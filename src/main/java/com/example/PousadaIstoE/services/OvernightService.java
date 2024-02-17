package com.example.PousadaIstoE.services;

import com.example.PousadaIstoE.Enums.PaymentStatus;
import com.example.PousadaIstoE.Enums.PaymentType;
import com.example.PousadaIstoE.exceptions.EntityConflict;
import com.example.PousadaIstoE.exceptions.EntityDates;
import com.example.PousadaIstoE.exceptions.EntityNotFound;
import com.example.PousadaIstoE.model.*;
import com.example.PousadaIstoE.repository.OvernightStayComsuptionRepository;
import com.example.PousadaIstoE.repository.OvernightStayRepository;
import com.example.PousadaIstoE.repository.RoomRepository;
import com.example.PousadaIstoE.request.CashRegisterRequest;
import com.example.PousadaIstoE.response.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Period;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import static com.example.PousadaIstoE.Enums.RoomStatus.*;
import static java.time.Period.*;

@Service
public class OvernightService {
    private static final Float ONE_PERSON_VALUE = 100F;
    private static final Float TWO_PERSONS_VALUE = 130F;
    private static final Float THREE_PERSONS_VALUE = 190F;
    private static final Float FOUR_PERSONS_VALUE = 210F;
    private static final Float FIVE_PERSONS_VALUE = 280F;
    private static final Float EMPTY = 0F;
    @PersistenceContext
    private EntityManager manager;
    private final OvernightStayRepository overnightStayRepository;
    private final RoomRepository roomRepository;
    private final CashRegisterService cashRegisterService;
    private final OvernightStayComsuptionRepository overnightStayComsuptionRepository;
    private final Finder find;
    private final OverNightStayConsumptionRepository overNightStayConsumptionRepository;

    protected OvernightService(
            OvernightStayRepository overnightStayRepository,
            RoomRepository roomRepository,
            CashRegisterService cashRegisterService,
            OvernightStayComsuptionRepository overnightStayComsuptionRepository, Finder find,
            OverNightStayConsumptionRepository overNightStayConsumptionRepository){
        this.overnightStayRepository = overnightStayRepository;
        this.roomRepository = roomRepository;
        this.cashRegisterService = cashRegisterService;
        this.overnightStayComsuptionRepository = overnightStayComsuptionRepository;
        this.find = find;
        this.overNightStayConsumptionRepository = overNightStayConsumptionRepository;
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

    private void changeReservationToOvernight(){}

    private void createOvernightStay(){}
    private void updateOvernightStay(){}


    private Float amountPeoplePrice(OvernightStay overnightStay) {
        Integer quantidadePessoa = overnightStay.getAmountPeople();
        return switch (quantidadePessoa) {
            case 1 -> {
                overnightStay.setTotal(ONE_PERSON_VALUE);
                yield ONE_PERSON_VALUE;
            }
            case 2 -> {
                overnightStay.setTotal(TWO_PERSONS_VALUE);
                yield TWO_PERSONS_VALUE;
            }
            case 3 -> {
                overnightStay.setTotal(THREE_PERSONS_VALUE);
                yield THREE_PERSONS_VALUE;
            }
            case 4 -> {
                overnightStay.setTotal(FOUR_PERSONS_VALUE);
                yield FOUR_PERSONS_VALUE;
            }
            case 5 -> {
                overnightStay.setTotal(FIVE_PERSONS_VALUE);
                yield FIVE_PERSONS_VALUE;
            }
            default -> throw new EntityConflict("Cannot be insert more than 5 peoples");
        }
    }
    private Float dailyValue(OvernightStay overnight) {
        int amountClient = overnight.getClientList().size();
        return switch (amountClient) {
            case 1 -> ONE_PERSON_VALUE;
            case 2 -> TWO_PERSONS_VALUE;
            case 3 -> THREE_PERSONS_VALUE;
            case 4 -> FOUR_PERSONS_VALUE;
            case 5 -> FIVE_PERSONS_VALUE;
            default -> EMPTY;
        };
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
        var totalConsumption = overNightStayConsumptionRepository.totalConsumption(overnight.getId());
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
                        dailyValue(overnight),
                        overnight.getPaymentType(),
                        overnight.getPaymentStatus(),
                        overnight.getTotal())
        );
    }





























    public Double valorTotal(Long id, OvernightStay overnightStay, Integer p1, Float total_diarias) {
        Double totalConsumo = manager.createQuery(
                        "SELECT sum(m.total) FROM OverNightStayConsumption m where m.overnightStay.id = :id", Double.class)
                .setParameter("id", id)
                .getSingleResult();
        if (totalConsumo == null) {
            totalConsumo = 0D;
        }
        return total_diarias + totalConsumo;
    }

    public OvernightStay createPernoite(OvernightStay overnightStay) {
        if (overnightStay.getClientList() == null) {
            throw new EntityConflict("É preciso informar o hóspede.");
        }
        Integer periodoDias = between(overnightStay.getStartDate(), overnightStay.getEndDate()).getDays();

        amountPeoplePrice(overnightStay);
        apartmentValidation(overnightStay);
        Float a = overnightStay.getTotal() * periodoDias;
        overnightStay.setTotal(a);
        if (overnightStay.getPaymentStatus() == null) {
            overnightStay.setPaymentStatus(PaymentStatus.PENDING);
        }
        if (overnightStay.getPaymentType() == null) {
            overnightStay.setPaymentType(PaymentType.PENDING);
        }
        return overnightStayRepository.save(overnightStay);
    }

//    public OvernightStay updatePernoiteData(Long pernoiteId, OvernightStay request) {
//        OvernightStay pernoite = overnightStayRepository.findById(pernoiteId)
//                .orElseThrow(() -> new EntityNotFound("Overnight Stay not Found."));
//
//        var pernoiteAtualizado = new OvernightStay(
//                pernoite.getId(),
//                pernoite.getRoom(),
//                pernoite.getClientList(),
//                pernoite.getStartDate(),
//                pernoite.getEndDate(),
//                pernoite.getAmountPeople(),
//                request.getPaymentType(),
//                request.getPaymentStatus(),
//                pernoite.getTotal()
//        );
//        return overnightStayRepository.save(pernoiteAtualizado);
//    }



    private void apartmentValidation(OvernightStay overnight) throws EntityConflict {
        List<OvernightStay> overnightStayCadastrados = overnightStayRepository.findByRoom_Id(overnight.getRoom().getId());
        if (overnight.getStartDate().isBefore(LocalDate.now()) || overnight.getEndDate().isBefore(LocalDate.now())) {
            throw new EntityDates("A data inserida não pode ser inferior a hoje");
        }
        switch (overnight.getRoom().getRoomStatus()) {
            case BUSY -> throw new EntityConflict("O apartment já está ocupado.");
            case NEEDS_CLEANING -> throw new EntityConflict("O apartment necessita de limpeza.");
            case RESERVED -> throw new EntityConflict("O apartment está reservado.");
            case MAINTENANCE -> throw new EntityConflict("O apartment está em manutenção.");
        }
        for (OvernightStay pernoiteCadastrado : overnightStayCadastrados) {
            if (overnight.getStartDate().isBefore(pernoiteCadastrado.getEndDate()) && overnight.getEndDate().isAfter(pernoiteCadastrado.getStartDate())) {
                throw new EntityConflict("O apartment já está ocupado entre as datas informadas.");
            }
            if (overnight.getEndDate().isBefore(overnight.getStartDate())) {
                throw new EntityDates("A data de Saída não pode ser inferior a data de cashIn");
            }
            if (overnight.getStartDate().equals(overnight.getEndDate()) || overnight.getEndDate().equals(overnight.getStartDate())) {
                throw new EntityDates("A data de Entrada/Saída não podem ser iguais");
            }
        }
        overnightStayRepository.save(overnight);
    }

    @Scheduled(cron = "0 * * * * ?") // Needs to be executed at midnight
    public void updateRoomStatus() {
        LocalDate currentDate = LocalDate.now();
        List<OvernightStay> overnightStayToCheckIn = overnightStayRepository.findByStartDate(currentDate);

        for (OvernightStay pernoiteIn : overnightStayToCheckIn) {
            Rooms roomsIn = pernoiteIn.getRoom();

            if (pernoiteIn.getStartDate().equals(currentDate)
                    && LocalTime.now().isAfter(LocalTime.of(6, 0))
                    && LocalTime.now().isBefore(LocalTime.of(12, 0))) {
                roomsIn.setRoomStatus(RESERVED);
                roomRepository.save(roomsIn);
            }
            if (pernoiteIn.getStartDate().equals(currentDate)
                    && LocalTime.now().isAfter(LocalTime.of(12, 0))
            ) {
                roomsIn.setRoomStatus(BUSY);
                roomRepository.save(roomsIn);
            }
        }
    }

    @Scheduled(cron = "0 1 12 * * ?")
    public void updateRoomStatusOut() {
        String report = "PERNOITE";
        LocalDate currentDate = LocalDate.now();
        List<OvernightStay> overnightStayToCheckOut = overnightStayRepository.findByEndDate(currentDate);

        for (OvernightStay overnightOut : overnightStayToCheckOut) {
            Rooms roomsOut = overnightOut.getRoom();
            if (overnightOut.getEndDate().equals(currentDate) && LocalTime.now().isAfter(LocalTime.of(12, 0))) {
                roomsOut.setRoomStatus(DAILY_CLOSED);
                roomRepository.save(roomsOut);
            }
            if (overnightOut.getEndDate().equals(currentDate)
                    && overnightOut.getPaymentStatus().equals(PaymentStatus.COMPLETED)) {

                switch (overnightOut.getPaymentType()) {
                    case PIX -> report += " (PIX)";
                    case CREDIT_CARD -> report += " (CARTÃO)";
                    case CASH -> report += " (DINHEIRO)";
                }
                CashRegisterRequest request = new CashRegisterRequest(
                        report,
                        overnightOut.getRoom().getNumber(),
                        overnightOut.getTotal(),
                        EMPTY);
                cashRegisterService.createCashRegister(request);
            }
        }
    }
}