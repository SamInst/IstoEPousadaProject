package com.example.PousadaIstoE.services;

import com.example.PousadaIstoE.Enums.PaymentStatus;
import com.example.PousadaIstoE.builders.OvernightBuilder;
import com.example.PousadaIstoE.exceptions.EntityConflict;
import com.example.PousadaIstoE.model.*;
import com.example.PousadaIstoE.repository.DailyValueRepository;
import com.example.PousadaIstoE.repository.OvernightStayRepository;
import com.example.PousadaIstoE.repository.RoomRepository;
import com.example.PousadaIstoE.request.CashRegisterRequest;
import com.example.PousadaIstoE.request.OvernightStayRequest;
import com.example.PousadaIstoE.response.ClientResponse;
import com.example.PousadaIstoE.response.OvernightStayResponse;
import com.example.PousadaIstoE.response.SimpleOvernightResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Period;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OvernightService {
    private static final Float EMPTY = 0F;
    private final DailyValueRepository dailyValueRepository;
    private final OvernightStayRepository overnightStayRepository;
    private final RoomRepository roomRepository;
    private final CashRegisterService cashRegisterService;
    private final Finder find;
    private final OverNightStayConsumptionRepository overNightStayConsumptionRepository;
    private final ClientService clientService;
    private final RoomService roomService;

    protected OvernightService(
            DailyValueRepository dailyValueRepository,
            OvernightStayRepository overnightStayRepository,
            RoomRepository roomRepository,
            CashRegisterService cashRegisterService,
            Finder find,
            OverNightStayConsumptionRepository overNightStayConsumptionRepository,
            ClientService clientService,
            RoomService roomService){
        this.dailyValueRepository = dailyValueRepository;
        this.overnightStayRepository = overnightStayRepository;
        this.roomRepository = roomRepository;
        this.cashRegisterService = cashRegisterService;
        this.find = find;
        this.overNightStayConsumptionRepository = overNightStayConsumptionRepository;
        this.clientService = clientService;
        this.roomService = roomService;
    }

    public Page<SimpleOvernightResponse> findAll(Pageable pageable){
        var allOvernights = overnightStayRepository.findAllOrderByIdDesc(pageable);

        List<SimpleOvernightResponse> responseList = allOvernights
                .stream()
                .map(this::simpleOvernightResponse)
                .sorted(Comparator.comparingLong(SimpleOvernightResponse::id).reversed())
                .collect(Collectors.toList());

        return new PageImpl<>(responseList, pageable, allOvernights.getTotalElements());
    }

    private SimpleOvernightResponse simpleOvernightResponse(OvernightStay overnightStay){
        List<String> names = overnightStay.getClientList().stream()
                .map(Client::getName)
                .collect(Collectors.toList());
        return new SimpleOvernightResponse(
                overnightStay.getId(),
                names,
                overnightStay.getRoom().getNumber(),
                overnightStay.getStartDate(),
                overnightStay.getEndDate(),
                overnightStay.getAmountPeople()
        );
    }

    public void changeReservationToOvernight(Long reservation_id){
        var reservation = find.reservationById(reservation_id);
        var room = find.roomByNumber(reservation.getRoom());
        roomService.roomVerification(room);

        OvernightStay overnight = new OvernightBuilder()
                .room(room)
                .clientList(reservation.getClientList())
                .startDate(reservation.getStartDate())
                .endDate(reservation.getEndDate())
                .amountPeople(reservation.getClientList().size())
                .paymentType(reservation.getPaymentType())
                .paymentStatus(reservation.getPaymentStatus())
                .totalConsumption(0F)
                .overnightValue(amountPeoplePrice(reservation.getClientList().size()))
                .total(amountPeoplePrice(reservation.getClientList().size()))
                .isActive(true)
                .build();
        overnightStayRepository.save(overnight);
    }

    public void createOvernightStay(OvernightStayRequest request){
        List<Client> clientList = new ArrayList<>();
        var room = find.roomById(request.room_id());
        roomService.roomVerification(room);

        request.client_list().forEach(clientService::clientRequest);
        clientService.clientListVerification(clientList);

        OvernightStay overnight = new OvernightBuilder()
                .room(room)
                .clientList(clientList)
                .startDate(request.start_date())
                .endDate(request.end_date())
                .amountPeople(clientList.size())
                .paymentType(request.payment_type())
                .paymentStatus(request.payment_status())
                .totalConsumption(0F)
                .overnightValue(amountPeoplePrice(clientList.size()))
                .isActive(true)
                .build();

        overnight.setTotal(overnight.getOvernightValue() + overnight.getTotalConsumption());
        overnightStayRepository.save(overnight);
    }

    public void updateOvernightStay(Long overnight_id, OvernightStayRequest request){
        var overnight = find.overnightStayById(overnight_id);
        var room = find.roomById(request.room_id());
        dataValidation(request.start_date(), request.end_date(), request.room_id());

        roomService.roomVerification(room);
        List<Client> updatedClientList = new ArrayList<>(overnight.getClientList());

        request.client_list().forEach(clientService::clientRequest);
        clientService.clientListVerification(updatedClientList);

        OvernightStay updatedOvernight = new OvernightBuilder()
                .id(overnight.getId())
                .room(room)
                .clientList(updatedClientList)
                .startDate(request.start_date())
                .endDate(request.end_date())
                .amountPeople(updatedClientList.size())
                .paymentType(request.payment_type())
                .paymentStatus(request.payment_status())
                .totalConsumption(overnight.getTotalConsumption())
                .overnightValue(amountPeoplePrice(updatedClientList.size()))
                .isActive(true)
                .build();

        if (request.payment_status().equals(PaymentStatus.COMPLETED)){
            updatedOvernight.setTotal(updatedOvernight.getOvernightValue() + updatedOvernight.getTotalConsumption());
            saveInCashRegister(updatedOvernight);
        }
        overnightStayRepository.save(updatedOvernight);
    }

    private Float sumConsumption(OvernightStay overnight){
        var totalConsumption = overNightStayConsumptionRepository.totalConsumptionByOvernightId(overnight.getId());
        totalConsumption = totalConsumption != null ? totalConsumption : 0.0;
        overnight.setTotalConsumption(totalConsumption.floatValue());
        return totalConsumption.floatValue();
    }


    private Float amountPeoplePrice(Integer amountPeople) {
        List<DailyValues> dailyValuesList = dailyValueRepository.findAll();
        for (DailyValues dailyValue : dailyValuesList) {
            if (amountPeople.equals(dailyValue.getAmountPeople())) {
                return dailyValue.getPrice();
            }
        }
        throw new IllegalStateException("Price not found for the specified amount of people: " + amountPeople);
    }

    public OvernightStayResponse findById(Long overnight_id){
        var overnight = find.overnightStayById(overnight_id);
        return overnightStayResponse(overnight);
    }

    private ClientResponse clientResponse(Client client){
       return new ClientResponse(
                client.getId(),
                client.getName(),
                client.getCpf(),
                client.getPhone(),
                client.getAddress(),
                client.getJob(),
                client.getRegisteredBy() != null ? client.getRegisteredBy().getName() : "",
                client.isHosted()
        );
    }

    private OvernightStayResponse overnightStayResponse(OvernightStay overnight){

        var amountDays = Period.between(overnight.getStartDate(), overnight.getEndDate()).getDays();
        var totalConsumption = overNightStayConsumptionRepository.totalConsumptionByOvernightId(overnight.getId());
        List<ClientResponse> clients = overnight.getClientList().stream()
                .map(this::clientResponse)
                .toList();
        return new OvernightStayResponse(
                overnight.getId(),
                clients,
                new OvernightStayResponse.Room(
                        overnight.getRoom().getNumber(),
                        overnight.getRoom().getRoomType()),
                overnight.getStartDate(),
                overnight.getEndDate(),
                new ArrayList<>(),
                new OvernightStayResponse.Values(
                        clients.size(),
                        amountDays,
                        totalConsumption,
                        amountPeoplePrice(clients.size()),
                        overnight.getPaymentType(),
                        overnight.getPaymentStatus(),
                        overnight.getTotal())
        );
    }

    private void dataValidation(LocalDate startDate, LocalDate endDate, Long room_id){
        var overnightList = overnightStayRepository.findAllByRoom_Id(room_id);
        if (startDate.equals(endDate)) throw new EntityConflict("The dates entered cannot be on the same day");
        if (endDate.isBefore(startDate)) throw new EntityConflict("The date entered cannot be less than today");

        for (OvernightStay overnight : overnightList) {
            if (overnight.getStartDate().isBefore(overnight.getEndDate())
                    && overnight.getEndDate().isAfter(overnight.getStartDate())) {
                throw new EntityConflict("The apartment is already occupied between the dates given.");
            }
        }
    }

    private void saveInCashRegister(OvernightStay overnightStay){
        String report = "PERNOITE";
        float cashOut = 0F;
        switch (overnightStay.getPaymentType()){
            case PIX -> {
                report += "(PIX)";
                cashOut += overnightStay.getTotal();
            }
            case CREDIT_CARD -> {
                report += "(CARTAO CREDITO)";
                cashOut += overnightStay.getTotal();
            }
            case DEBIT_CARD -> {
                report += "(CARTAO DEBITO)";
                cashOut += overnightStay.getTotal();
            }
            case CASH -> report += "(DINHEIRO)";
        }
        CashRegisterRequest cashRegisterRequest = new CashRegisterRequest(
                report,
                overnightStay.getRoom().getNumber(),
                overnightStay.getTotal(),
                cashOut);
        cashRegisterService.createCashRegister(cashRegisterRequest);
    }


//    @Scheduled(cron = "0 * * * * ?") // Needs to be executed at midnight
//    public void updateRoomStatus() {
//        LocalDate currentDate = LocalDate.now();
//        List<OvernightStay> overnightStayToCheckIn = overnightStayRepository.findByStartDate(currentDate);
//
//        for (OvernightStay overnightStay : overnightStayToCheckIn) {
//            Rooms roomsIn = overnightStay.getRoom();
//
//            if (overnightStay.getStartDate().equals(currentDate)
//                    && LocalTime.now().isAfter(LocalTime.of(6, 0))
//                    && LocalTime.now().isBefore(LocalTime.of(12, 0))) {
//                roomsIn.setRoomStatus(RESERVED);
//                roomRepository.save(roomsIn);
//            }
//            if (overnightStay.getStartDate().equals(currentDate)
//                    && LocalTime.now().isAfter(LocalTime.of(12, 0))
//            ) {
//                roomsIn.setRoomStatus(BUSY);
//                roomRepository.save(roomsIn);
//            }
//        }
//    }
//
//    @Scheduled(cron = "0 1 12 * * ?")
//    public void updateRoomStatusOut() {
//        String report = "PERNOITE";
//        LocalDate currentDate = LocalDate.now();
//        List<OvernightStay> overnightStayToCheckOut = overnightStayRepository.findByEndDate(currentDate);
//
//        for (OvernightStay overnightOut : overnightStayToCheckOut) {
//            Rooms roomsOut = overnightOut.getRoom();
//            if (overnightOut.getEndDate().equals(currentDate) && LocalTime.now().isAfter(LocalTime.of(12, 0))) {
//                roomsOut.setRoomStatus(DAILY_CLOSED);
//                roomRepository.save(roomsOut);
//            }
//            if (overnightOut.getEndDate().equals(currentDate)
//                    && overnightOut.getPaymentStatus().equals(PaymentStatus.COMPLETED)) {
//
//                switch (overnightOut.getPaymentType()) {
//                    case PIX -> report += " (PIX)";
//                    case CREDIT_CARD -> report += " (CARTÃO)";
//                    case CASH -> report += " (DINHEIRO)";
//                }
//                CashRegisterRequest request = new CashRegisterRequest(
//                        report,
//                        overnightOut.getRoom().getNumber(),
//                        overnightOut.getTotal(),
//                        EMPTY);
//                cashRegisterService.createCashRegister(request);
//            }
//        }
//    }
}