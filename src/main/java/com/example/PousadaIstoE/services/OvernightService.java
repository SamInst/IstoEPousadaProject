package com.example.PousadaIstoE.services;

import com.example.PousadaIstoE.Enums.OvernightStayStatus;
import com.example.PousadaIstoE.Enums.RoomStatus;
import com.example.PousadaIstoE.builders.OvernightBuilder;
import com.example.PousadaIstoE.exceptions.EntityConflict;
import com.example.PousadaIstoE.model.*;
import com.example.PousadaIstoE.repository.*;
import com.example.PousadaIstoE.request.CashRegisterRequest;
import com.example.PousadaIstoE.request.OvernightStayRequest;
import com.example.PousadaIstoE.request.PaymentRequest;
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
    private static final long CASH = 2L;
    private final DailyValueRepository dailyValueRepository;
    private final OvernightStayRepository overnightStayRepository;
    private final CashRegisterService cashRegisterService;
    private final Finder find;
    private final OverNightStayConsumptionRepository overNightStayConsumptionRepository;
    private final CustomerService customerService;
    private final RoomService roomService;
    private final OvernightStayReservationRepository overnightStayReservationRepository;
    private final PaymentService paymentService;

    protected OvernightService(
            DailyValueRepository dailyValueRepository,
            OvernightStayRepository overnightStayRepository,
            CashRegisterService cashRegisterService,
            Finder find,
            OverNightStayConsumptionRepository overNightStayConsumptionRepository,
            CustomerService customerService,
            RoomService roomService,
            OvernightStayReservationRepository overnightStayReservationRepository, PaymentService paymentService
    ) {
        this.dailyValueRepository = dailyValueRepository;
        this.overnightStayRepository = overnightStayRepository;
        this.cashRegisterService = cashRegisterService;
        this.find = find;
        this.overNightStayConsumptionRepository = overNightStayConsumptionRepository;
        this.customerService = customerService;
        this.roomService = roomService;
        this.overnightStayReservationRepository = overnightStayReservationRepository;
        this.paymentService = paymentService;
    }

    public Page<SimpleOvernightResponse> findAll(Pageable pageable) {
        var allOvernights = overnightStayRepository.findAllOrderByIdDesc(pageable);

        List<SimpleOvernightResponse> responseList = allOvernights
                .stream()
                .map(this::simpleOvernightResponse)
                .sorted(Comparator.comparingLong(SimpleOvernightResponse::id).reversed())
                .collect(Collectors.toList());

        return new PageImpl<>(responseList, pageable, allOvernights.getTotalElements());
    }

    private SimpleOvernightResponse simpleOvernightResponse(OvernightStay overnightStay) {
        List<String> names = overnightStay.getCustomerList().stream()
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

    public void changeReservationToOvernight(Long reservation_id) {
        var reservation = find.reservationById(reservation_id);
        var room = find.roomByNumber(reservation.getRoom());
        var period = Period.between(reservation.getStartDate(), reservation.getEndDate()).getDays();
        var overnightValue = amountPeoplePrice(reservation.getCustomer().size());
        var total = period * overnightValue;
        var payments = paymentService.findAllByReservationId(reservation_id);

        roomService.roomVerification(room);

        List<Customer> customerList = new ArrayList<>(reservation.getCustomer());

        customerList.forEach(client -> customerService.customerHosted(client, true));

        OvernightStay overnight = new OvernightBuilder()
                .room(room)
                .clientList(customerList)
                .startDate(reservation.getStartDate())
                .endDate(reservation.getEndDate())
                .amountPeople(reservation.getCustomer().size())
                .totalConsumption(0F)
                .overnightValue(overnightValue)
                .status(OvernightStayStatus.ACTIVE)
                .total(total)
                .isActive(true)
                .obs(reservation.getObs())
                .build();

        var savedOvernight = overnightStayRepository.save(overnight);
        reservation.setIsActive(false);
        overnightStayReservationRepository.save(reservation);
        roomService.updateRoomStatus(room.getId(), RoomStatus.BUSY);

        List<PaymentRequest> paymentList = new ArrayList<>();
        payments.forEach(payment -> {
            PaymentRequest request = new PaymentRequest(
                    payment.getPaymentType().getId(),
                    payment.getValue(),
                    payment.getPaymentStatus());
            paymentList.add(request);
        });
        paymentService.addCalculatePaymentTypeOvernight(savedOvernight.getId(), paymentList);
    }

    public void createOvernightStay(OvernightStayRequest request) {
        List<Customer> customerList = new ArrayList<>();
        var room = find.roomById(request.room_id());
        var overnightValue = amountPeoplePrice(request.customer_list().size());
        var period = Period.between(request.start_date(), request.end_date()).getDays();
        var total = period * overnightValue;
        dataValidation(request.start_date(), request.end_date(), request.room_id());
        roomService.roomVerification(room);

        request.customer_list().forEach(client -> {
            customerService.customerVerification(client, customerList);
            customerService.customerListVerification(customerList);
            customerList.forEach(customer -> customerService.customerHosted(customer, true));
        });
        OvernightStay overnight = new OvernightBuilder()
                .room(room)
                .clientList(customerList)
                .startDate(request.start_date())
                .endDate(request.end_date())
                .amountPeople(customerList.size())
                .totalConsumption(0F)
                .overnightValue(overnightValue)
                .total(total)
                .status(OvernightStayStatus.ACTIVE)
                .isActive(true)
                .obs(request.obs())
                .build();
        var savedOvernight = overnightStayRepository.save(overnight);

        roomService.updateRoomStatus(room.getId(), RoomStatus.BUSY);

        paymentService.addCalculatePaymentTypeOvernight(savedOvernight.getId(), request.payment_type());
    }

    public void updateOvernightStay(Long overnight_id, OvernightStayRequest request) {
        var overnight = find.overnightStayById(overnight_id);
        List<Customer> updatedCustomerList = new ArrayList<>(overnight.getCustomerList());

        var room = find.roomById(request.room_id());
        var overnightValue = amountPeoplePrice(updatedCustomerList.size());
        var period = Period.between(request.start_date(), request.end_date()).getDays();

        dataValidation(request.start_date(), request.end_date(), request.room_id());

        if (!request.room_id().equals(overnight.getRoom().getId())) {
            roomService.roomVerification(room);
            roomService.updateRoomStatus(overnight.getRoom().getId(), RoomStatus.AVAILABLE);
        }

        if (!request.customer_list().isEmpty()) {
            request.customer_list().forEach(client -> customerService.customerVerification(client, updatedCustomerList));
            customerService.customerListVerification(updatedCustomerList);
        }
        OvernightStay updatedOvernight = new OvernightBuilder()
                .id(overnight.getId())
                .room(room)
                .clientList(updatedCustomerList)
                .startDate(request.start_date())
                .endDate(request.end_date())
                .amountPeople(updatedCustomerList.size())
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
            updatedOvernight.setActive(false);
            saveInCashRegister(updatedOvernight);
            setCustomersHostedFalse(updatedOvernight);
            roomService.updateRoomStatus(updatedOvernight.getRoom().getId(), RoomStatus.AVAILABLE);
        }
        overnightStayRepository.save(updatedOvernight);
    }

    private void setCustomersHostedFalse(OvernightStay overnightStay){
        overnightStay.getCustomerList().forEach(customer -> customerService.customerHosted(customer, false));
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
                consumption.getItem().getDescription(),
                consumption.getItem().getValue(),
                consumption.getTotal()
        );
    }


    private OvernightStayResponse overnightStayResponse(OvernightStay overnight){
        var amountDays = Period.between(overnight.getStartDate(), overnight.getEndDate()).getDays();
        var totalConsumption = sumConsumption(overnight);
        var consumption = overNightStayConsumptionRepository.findAllByOvernightStay_Id(overnight.getId());
        var total = (amountDays * overnight.getOvernightValue()) + totalConsumption;

        List<CustomerResponse> customerResponses = new ArrayList<>();
        List<PaymentResponse> paymentTypeList = paymentService.findAllByOvernightId(overnight.getId()).stream()
                .map(PaymentService::paymentOvernightResponse)
                .toList();

        List<ConsumptionResponse> consumptionsResponse = consumption.stream()
                .map(this::consumptionResponse)
                .toList();

        overnight.getCustomerList().forEach(client -> customerResponses.add(customerService.customerResponse(client)));

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
                        amountPeoplePrice(overnight.getCustomerList().size()),
                        paymentTypeList,
                        total),
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
                        //need to test
                        && overnight.getEndDate().isAfter(overnight.getStartDate())) {
                    throw new EntityConflict("The apartment is already occupied between the dates given.");
                }
            }
        }
    }

    private void saveInCashRegister(OvernightStay overnightStay) {
    var cash = find.paymentById(CASH);
    AtomicReference<String> report = new AtomicReference<>("PERNOITE ");
    AtomicReference<Float> cashIn = new AtomicReference<>(0F);

    var paymentTypeList = paymentService.findAllByOvernightId(overnightStay.getId());
    paymentTypeList.forEach(paymentType -> {
        if (paymentTypeList.stream().anyMatch(payment -> payment.getPaymentType().equals(cash)) && paymentTypeList.size() == 1){
            report.updateAndGet(v -> v + "(" + paymentType.getPaymentType().getDescription() + ")");
            cashIn.set(overnightStay.getTotal());
        } else {
            report.updateAndGet(v -> v + "(" + paymentType.getPaymentType().getDescription()
                    + " 'R$ "+ paymentType.getValue()+ "') ");

            paymentTypeList.stream()
                    .filter(payment -> payment.getPaymentType().equals(cash))
                    .findFirst()
                    .ifPresent(payment -> cashIn.set(payment.getValue()));
        }
    });
    CashRegisterRequest cashRegisterRequest = new CashRegisterRequest(
            report.get(),
            overnightStay.getRoom().getNumber(),
            cashIn.get(),
            0F);
    cashRegisterService.createCashRegister(cashRegisterRequest);
    }
}