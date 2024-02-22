package com.example.PousadaIstoE.services;

import com.example.PousadaIstoE.Enums.OvernightStayStatus;
import com.example.PousadaIstoE.Enums.RoomStatus;
import com.example.PousadaIstoE.builders.OvernightBuilder;
import com.example.PousadaIstoE.exceptions.EntityConflict;
import com.example.PousadaIstoE.model.*;
import com.example.PousadaIstoE.repository.*;
import com.example.PousadaIstoE.request.CashRegisterRequest;
import com.example.PousadaIstoE.request.OvernightStayRequest;
import com.example.PousadaIstoE.response.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
public class OvernightService {
    private static final long PIX = 6L;
    private static final long BANK_TRANSFER = 5L;
    private static final long DEBIT_CARD = 4L;
    private static final long CREDIT_CARD = 3L;
    private static final long CASH = 2L;
    private static final long PENDING = 1L;

    private final DailyValueRepository dailyValueRepository;
    private final OvernightStayRepository overnightStayRepository;
    private final CashRegisterService cashRegisterService;
    private final Finder find;
    private final OverNightStayConsumptionRepository overNightStayConsumptionRepository;
    private final CustomerService customerService;
    private final RoomService roomService;
    private final OvernightStayReservationRepository overnightStayReservationRepository;

    protected OvernightService(
            DailyValueRepository dailyValueRepository,
            OvernightStayRepository overnightStayRepository,
            CashRegisterService cashRegisterService,
            Finder find,
            OverNightStayConsumptionRepository overNightStayConsumptionRepository,
            CustomerService customerService,
            RoomService roomService,
            OvernightStayReservationRepository overnightStayReservationRepository){
        this.dailyValueRepository = dailyValueRepository;
        this.overnightStayRepository = overnightStayRepository;
        this.cashRegisterService = cashRegisterService;
        this.find = find;
        this.overNightStayConsumptionRepository = overNightStayConsumptionRepository;
        this.customerService = customerService;
        this.roomService = roomService;
        this.overnightStayReservationRepository = overnightStayReservationRepository;
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
                .map(Customer::getName)
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
        var period = Period.between(reservation.getStartDate(), reservation.getEndDate()).getDays();
        var overnightValue = amountPeoplePrice(reservation.getClient().size());
        var total = period * overnightValue;

        roomService.roomVerification(room);

        List<Customer> customerList = new ArrayList<>(reservation.getClient());
        List<PaymentType> paymentTypeList = new ArrayList<>(reservation.getPaymentType());
        customerList.forEach(client ->{ customerService.customerHosted(client, true); });

        OvernightStay overnight = new OvernightBuilder()
                .room(room)
                .clientList(customerList)
                .startDate(reservation.getStartDate())
                .endDate(reservation.getEndDate())
                .amountPeople(reservation.getClient().size())
                .paymentType(paymentTypeList)
                .paymentStatus(reservation.getPaymentStatus())
                .totalConsumption(0F)
                .overnightValue(overnightValue)
                .status(OvernightStayStatus.ACTIVE)
                .total(total)
                .isActive(true)
                .obs(reservation.getObs())
                .build();

        overnightStayRepository.save(overnight);
        reservation.setActive(false);
        overnightStayReservationRepository.save(reservation);
        roomService.updateRoomStatus(room.getId(), RoomStatus.BUSY);
    }

    public void createOvernightStay(OvernightStayRequest request){
        List<Customer> customerList = new ArrayList<>();
        List<PaymentType> paymentTypeList = new ArrayList<>();
        var room = find.roomById(request.room_id());
        var overnightValue = amountPeoplePrice(request.client_list().size());
        var period = Period.between(request.start_date(), request.end_date()).getDays();
        var total = period * overnightValue;

        roomService.roomVerification(room);

        request.client_list().forEach(client -> { customerService.customerVerification(client, customerList); });
        customerService.customerListVerification(customerList);

        request.payment_type_id().forEach(id -> {
            var paymentType = find.paymentById(id);
            paymentTypeList.add(paymentType);
        });

        OvernightStay overnight = new OvernightBuilder()
                .room(room)
                .clientList(customerList)
                .startDate(request.start_date())
                .endDate(request.end_date())
                .amountPeople(customerList.size())
                .paymentType(paymentTypeList)
                .paymentStatus(request.payment_status())
                .totalConsumption(0F)
                .overnightValue(overnightValue)
                .total(total)
                .status(OvernightStayStatus.ACTIVE)
                .isActive(true)
                .obs(request.obs())
                .build();
        overnightStayRepository.save(overnight);
    }

    public void updateOvernightStay(Long overnight_id, OvernightStayRequest request){
        var overnight = find.overnightStayById(overnight_id);
        List<Customer> updatedCustomerList = new ArrayList<>(overnight.getClientList());
        List<PaymentType> paymentTypeList = new ArrayList<>();
        var room = find.roomById(request.room_id());
        var overnightValue = amountPeoplePrice(updatedCustomerList.size());
        var period = Period.between(request.start_date(), request.end_date()).getDays();


        dataValidation(request.start_date(), request.end_date(), request.room_id());

        if (!request.room_id().equals(overnight.getRoom().getId())){
            roomService.roomVerification(room);
        }

        if (!request.client_list().isEmpty()){
            request.client_list().forEach(client ->{
                customerService.customerVerification(client, updatedCustomerList);
            });
            customerService.customerListVerification(updatedCustomerList);
        }

        if (!request.payment_type_id().isEmpty()){
            request.payment_type_id().forEach(id -> {
                var paymentType = find.paymentById(id);
                paymentTypeList.add(paymentType);
            });
        }
        OvernightStay updatedOvernight = new OvernightBuilder()
                .id(overnight.getId())
                .room(room)
                .clientList(updatedCustomerList)
                .startDate(request.start_date())
                .endDate(request.end_date())
                .amountPeople(updatedCustomerList.size())
                .paymentType(paymentTypeList)
                .paymentStatus(request.payment_status())
                .totalConsumption(overnight.getTotalConsumption())
                .overnightValue(overnightValue)
                .status(request.overnightStatus())
                .isActive(true)
                .obs(request.obs())
                .build();

        var totalConsumption = sumConsumption(updatedOvernight);
        var total = (period * overnightValue) + totalConsumption;
        updatedOvernight.setTotal(total);

        if (request.overnightStatus().equals(OvernightStayStatus.FINISHED)){

            updatedOvernight.setTotalConsumption(totalConsumption);
            updatedOvernight.setTotal(total);
            saveInCashRegister(updatedOvernight);
            roomService.updateRoomStatus(updatedOvernight.getRoom().getId(), RoomStatus.AVAILABLE);
        }
        overnightStayRepository.save(updatedOvernight);
    }

    private Float sumConsumption(OvernightStay overnight){
        var totalConsumption = overNightStayConsumptionRepository.totalConsumptionByOvernightId(overnight.getId());
        totalConsumption = totalConsumption != null ? totalConsumption : 0.0F;
        overnight.setTotalConsumption(totalConsumption);
        return totalConsumption;
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

    private ConsumptionResponse consumptionResponse(OverNightStayConsumption consumption){
        return new ConsumptionResponse(
                consumption.getId(),
                consumption.getAmount(),
                consumption.getItens().getDescription(),
                consumption.getItens().getValue(),
                consumption.getTotal()
        );
    }

    private OvernightStayResponse overnightStayResponse(OvernightStay overnight){
        var amountDays = Period.between(overnight.getStartDate(), overnight.getEndDate()).getDays();
        var totalConsumption = overNightStayConsumptionRepository.totalConsumptionByOvernightId(overnight.getId());
        var consumption = overNightStayConsumptionRepository.findAllByOvernightStay_Id(overnight.getId());

        List<CustomerResponse> customerResponses = new ArrayList<>();
        List<PaymentTypeResponse> paymentTypeList = new ArrayList<>();
        List<ConsumptionResponse> consumptionsResponse = consumption.stream()
                .map(this::consumptionResponse)
                .toList();

        overnight.getClientList().forEach(client -> { customerResponses.add(customerService.customerResponse(client)); });
        overnight.getPaymentType().forEach(type ->{
            PaymentTypeResponse response = new PaymentTypeResponse(type.getDescription());
            paymentTypeList.add(response);
        });

        return new OvernightStayResponse(
                overnight.getId(),
                customerResponses,
                new OvernightStayResponse.Room(
                        overnight.getRoom().getNumber(),
                        overnight.getRoom().getRoomType()),
                overnight.getStartDate(),
                overnight.getEndDate(),
                consumptionsResponse,
                new OvernightStayResponse.Values(
                        customerResponses.size(),
                        amountDays,
                        totalConsumption,
                        amountPeoplePrice(overnight.getClientList().size()),
                        paymentTypeList,
                        overnight.getPaymentStatus(),
                        overnight.getTotal() + totalConsumption),
                overnight.getObs()
        );
    }

    private void dataValidation(LocalDate startDate, LocalDate endDate, Long room_id){
        var overnightList = overnightStayRepository.findAllByRoom_Id(room_id);
        if (startDate.equals(endDate)) throw new EntityConflict("The dates entered cannot be on the same day");
        if (endDate.isBefore(startDate)) throw new EntityConflict("The date entered cannot be less than today");

        for (OvernightStay overnight : overnightList) {
            if (!overnight.getRoom().getId().equals(room_id)){
                if (overnight.getStartDate().isBefore(overnight.getEndDate())
                        && overnight.getEndDate().isAfter(overnight.getStartDate())) {
                    throw new EntityConflict("The apartment is already occupied between the dates given.");
                }
            }
        }
    }

    private void saveInCashRegister(OvernightStay overnightStay) {
    AtomicReference<String> report = new AtomicReference<>("PERNOITE ");
    AtomicReference<Float> cashOut = new AtomicReference<>(0F);

    List<PaymentType> paymentTypes = overnightStay.getPaymentType();
    paymentTypes.forEach(paymentType -> {
        report.updateAndGet(v -> v + "(" + paymentType.getDescription() + ") ");
        if (paymentType.getId() == PIX
            || paymentType.getId() == CREDIT_CARD
            || paymentType.getId() == DEBIT_CARD
            || paymentType.getId() == BANK_TRANSFER) {
            cashOut.updateAndGet(v -> v + overnightStay.getTotal());
        } else if (paymentType.getId() == CASH) {
            cashOut.set(0F);
        }
    });
    CashRegisterRequest cashRegisterRequest = new CashRegisterRequest(
            report.get(),
            overnightStay.getRoom().getNumber(),
            overnightStay.getTotal(),
            cashOut.get());
    cashRegisterService.createCashRegister(cashRegisterRequest);
    }
}