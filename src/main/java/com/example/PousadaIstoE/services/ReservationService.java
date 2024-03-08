package com.example.PousadaIstoE.services;

import com.example.PousadaIstoE.builders.ReservationBuilder;
import com.example.PousadaIstoE.exceptions.EntityConflict;
import com.example.PousadaIstoE.model.*;
import com.example.PousadaIstoE.repository.DailyValuesRepository;
import com.example.PousadaIstoE.repository.OvernightStayReservationRepository;
import com.example.PousadaIstoE.request.ReservationRequest;
import com.example.PousadaIstoE.request.UpdateReservationRequest;
import com.example.PousadaIstoE.response.PaymentResponse;
import com.example.PousadaIstoE.response.CustomerResponse;
import com.example.PousadaIstoE.response.ReservationResponse;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReservationService {
    private final OvernightStayReservationRepository overnightStayReservationRepository;
    private final CustomerService customerService;
    private final Finder find;
    private final DailyValuesRepository dailyValuesRepository;
    private final PaymentService paymentService;


    public ReservationService(
            OvernightStayReservationRepository overnightStayReservationRepository,
            CustomerService customerService,
            Finder find,
            DailyValuesRepository dailyValuesRepository, PaymentService paymentService
    ){
        this.overnightStayReservationRepository = overnightStayReservationRepository;
        this.customerService = customerService;
        this.find = find;
        this.dailyValuesRepository = dailyValuesRepository;
        this.paymentService = paymentService;
    }

    public ReservationResponse findReservationById(Long reservation_id){
        var reservation = find.reservationById(reservation_id);
        return reservationResponse(reservation);
    }

    public List<ReservationResponse> allReservationsByStartDate(LocalDate startDate) {
        List<ReservationResponse> reservationResponseList = new ArrayList<>();
        var reservations = overnightStayReservationRepository.findAllByStartDateAndActiveIsTrue(startDate);

        for (Reservation reservation : reservations) reservationResponseList.add(reservationResponse(reservation));
        return reservationResponseList;
    }

    public void createReservation(ReservationRequest request) {
        List<Customer> customerList = new ArrayList<>();

        dateValidation(request.start_date(), request.end_date());

        request.clients().forEach(client -> { customerService.customerVerification(client, customerList); });
        customerService.customerListVerification(customerList);

        Reservation reservation = new ReservationBuilder()
                .startDate(request.start_date())
                .endDate(request.end_date())
                .room(request.room())
                .obs(request.obs().toUpperCase())
                .isActive(true)
                .build();
        isRoomAvailable(reservation, request.room());
        reservation.setCustomer(customerList);
       var savedReservation = overnightStayReservationRepository.save(reservation);

        request.paymentType().forEach(payment -> {
            var paymentType = find.paymentById(payment.payment_type_id());

            PaymentReservation paymentReservation = new PaymentReservation(
                    paymentType,
                    payment.value(),
                    payment.status(),
                    savedReservation);
            paymentService.savePaymentReservation(paymentReservation);
        });
    }

    public void alterReservation(Long reservation_id, UpdateReservationRequest request){
        var reservation = find.reservationById(reservation_id);
        dateValidation(reservation.getStartDate(), reservation.getEndDate());

        if (!request.room().equals(reservation.getRoom())
                || !request.start_date().equals(reservation.getStartDate())
                || !request.end_date().equals(reservation.getEndDate())){
                isRoomAvailable(reservation, request.room());
        }

        List<Customer> customerListUpdated = new ArrayList<>(reservation.getCustomer());

        if(!request.clients().isEmpty()) { request.clients().forEach(
                client -> { customerService.customerVerification(client, customerListUpdated); }); }

        customerService.customerListVerification(customerListUpdated);

        Reservation updateReservation = new ReservationBuilder()
            .id(reservation.getId())
            .startDate(request.start_date())
            .endDate(request.end_date())
            .clientList(customerListUpdated)
            .room(request.room())
            .isActive(reservation.getIsActive())
            .obs(request.obs() != null ? request.obs().toUpperCase() : "")
            .build();
        overnightStayReservationRepository.save(updateReservation);
    }

    public void removeClientFromReservation(Long reservation_id, Long client_id) {
        var client = find.clientById(client_id);
        var reservation = find.reservationById(reservation_id);

        if (reservation != null && !reservation.getCustomer().isEmpty()) {
             reservation.getCustomer().remove(client);
             overnightStayReservationRepository.save(reservation);
        }
    }

    private Float calculateTotal(Reservation reservation){
        var period = Period.between(reservation.getStartDate(), reservation.getEndDate()).getDays();
        var amountPeople = reservation.getCustomer().size();
        var dailyValue = amountPeoplePrice(amountPeople);
        return dailyValue * period;
    }

    private ReservationResponse reservationResponse(Reservation reservation){
        var total = calculateTotal(reservation);

        List<PaymentResponse> paymentTypeList = paymentService.findAllByReservationId(reservation.getId())
                .stream()
                .map(PaymentService::paymentReservationResponse).toList();
        List<CustomerResponse> customerResponseList = new ArrayList<>();


        if (reservation.getCustomer() != null) {
            reservation.getCustomer().forEach(client -> {
                CustomerResponse customerResponse = new CustomerResponse(
                        client.getId(),
                        client.getName(),
                        client.getCpf(),
                        client.getPhone(),
                        client.getAddress(),
                        client.getJob(),
                        client.getRegisteredBy() != null ? client.getRegisteredBy().getName() : "",
                        client.getCountry() != null ? client.getCountry().getDescription() : "",
                        client.getState() != null ? client.getState().getDescription() : "",
                        client.getCounty() != null ? client.getCounty().getDescription() : "",
                        client.getIsHosted()
                );
                customerResponseList.add(customerResponse);
            });
        }
        return new ReservationResponse(
                reservation.getId(),
                customerResponseList,
                reservation.getStartDate(),
                reservation.getEndDate(),
                reservation.getRoom(),
                paymentTypeList,
                customerResponseList.size(),
                reservation.getObs(),
                reservation.getIsActive(),
                total
                );
    }

    private void dateValidation(LocalDate startDate, LocalDate endDate){
        if (startDate.equals(endDate)) throw new EntityConflict("The dates entered cannot be on the same day");
        if (endDate.isBefore(startDate)) throw new EntityConflict("The date entered cannot be less than today");
    }

    public void isRoomAvailable(Reservation reservation, Integer room_number) {
        List<Reservation> reservationsList = overnightStayReservationRepository.findAllByRoomAndIsActiveIsTrue(room_number);

        for (Reservation existingReservation : reservationsList) {
            if (isOverlap(existingReservation, reservation)) {
                throw new EntityConflict("The Room has busy on date entered");
            }
        }
    }
    private boolean isOverlap(Reservation existingReservation, Reservation newReservation) {
        return !(newReservation.getEndDate().isBefore(existingReservation.getStartDate()) ||
                newReservation.getStartDate().isAfter(existingReservation.getEndDate().minusDays(1)));
    }

    public void cancelReservation(Long reservation_id){
        var reservation = find.reservationById(reservation_id);
        reservation.setIsActive(false);
        overnightStayReservationRepository.save(reservation);
    }

    private Float amountPeoplePrice(Integer amountPeople) {
        List<DailyValues> dailyValuesList = dailyValuesRepository.findAll();
        for (DailyValues dailyValue : dailyValuesList) {
            if (amountPeople.equals(dailyValue.getAmountPeople())) {
                return dailyValue.getPrice();
            }
        }
        throw new IllegalStateException("Price not found for the specified amount of people: " + amountPeople);
    }

    @Scheduled(cron = "0 0 6 * * *") // Runs at 6:00 AM every day
    public void removeReservations() {
        var canceledReservations = overnightStayReservationRepository.findAllAndActiveIsFalse();
        canceledReservations.forEach(reservation -> {
            if (reservation.getEndDate().isAfter(LocalDate.now().plusDays(7))){
                reservation.getCustomer().forEach(client -> {
                    System.out.println("removed reservation of ID " + reservation.getId() + " of client" + client.getName());
                    removeClientFromReservation(reservation.getId(), client.getId());
                });
            }
            overnightStayReservationRepository.deleteById(reservation.getId());
        });
    }
}
