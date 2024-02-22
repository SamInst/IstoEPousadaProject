package com.example.PousadaIstoE.services;

import com.example.PousadaIstoE.builders.ReservationBuilder;
import com.example.PousadaIstoE.exceptions.EntityConflict;
import com.example.PousadaIstoE.model.*;
import com.example.PousadaIstoE.repository.DailyValuesRepository;
import com.example.PousadaIstoE.repository.OvernightStayReservationRepository;
import com.example.PousadaIstoE.request.ReservationRequest;
import com.example.PousadaIstoE.request.UpdateReservationRequest;
import com.example.PousadaIstoE.response.CustomerResponse;
import com.example.PousadaIstoE.response.PaymentTypeResponse;
import com.example.PousadaIstoE.response.ReservationResponse;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

@Service
public class ReservationService {
    private final OvernightStayReservationRepository overnightStayReservationRepository;
    private final CustomerService customerService;
    private final Finder find;
    private final DailyValuesRepository dailyValuesRepository;

    public ReservationService(
            OvernightStayReservationRepository overnightStayReservationRepository,
            CustomerService customerService,
            Finder find,
            DailyValuesRepository dailyValuesRepository
            ){
        this.overnightStayReservationRepository = overnightStayReservationRepository;
        this.customerService = customerService;
        this.find = find;
        this.dailyValuesRepository = dailyValuesRepository;
    }

    public ReservationResponse findReservationById(Long reservation_id){
        var reservation = find.reservationById(reservation_id);
        return reservationResponse(reservation);
    }

    public List<ReservationResponse> allReservationsByStartDate(LocalDate startDate) {
        List<ReservationResponse> reservationResponseList = new ArrayList<>();
        var reservations = overnightStayReservationRepository.findAllByStartDateAndActiveIsTrue(startDate);

        for (OvernightStayReservation reservation : reservations) {
            reservationResponseList.add(reservationResponse(reservation));
        }
        return reservationResponseList;
    }

    public void createReservation(ReservationRequest request) {
        List<Customer> customerList = new ArrayList<>();
        List<PaymentType> paymentTypeList = new ArrayList<>();
        dateValidation(request.start_date(), request.end_date());

        request.clients().forEach(client -> { customerService.customerVerification(client, customerList); });

        request.payment_type_id().forEach(id -> {
            var paymentType = find.paymentById(id);
            paymentTypeList.add(paymentType);
        });

        customerService.customerListVerification(customerList);

        OvernightStayReservation reservation = new ReservationBuilder()
                .startDate(request.start_date())
                .endDate(request.end_date())
                .room(request.room())
                .paymentType(paymentTypeList)
                .paymentStatus(request.payment_status())
                .obs(request.obs().toUpperCase())
                .isActive(true)
                .build();
        isRoomAvailable(reservation);
        reservation.setClient(customerList);
        overnightStayReservationRepository.save(reservation);
    }



    public void alterReservation(Long reservation_id, UpdateReservationRequest request){
        var reservation = find.reservationById(reservation_id);
        dateValidation(reservation.getStartDate(), reservation.getEndDate());

        List<Customer> customerListUpdated = new ArrayList<>(reservation.getClient());
        List<PaymentType> paymentTypeList = new ArrayList<>();

        if(!request.clients().isEmpty()) { request.clients().forEach(
                client -> { customerService.customerVerification(client, customerListUpdated); }); }

        if (!request.payment_type_id().isEmpty()){
            request.payment_type_id().forEach(id -> {

                    var paymentType = find.paymentById(id);
                    paymentTypeList.add(paymentType);

            });
        }
        customerService.customerListVerification(customerListUpdated);

        OvernightStayReservation updateReservation = new ReservationBuilder()
            .id(reservation.getId())
            .startDate(request.start_date())
            .endDate(request.end_date())
            .clientList(customerListUpdated)
            .room(request.room())
            .paymentType(paymentTypeList)
            .paymentStatus(request.payment_status())
            .isActive(reservation.getActive())
            .obs(request.obs() != null ? request.obs().toUpperCase() : "")
            .build();
        overnightStayReservationRepository.save(updateReservation);
    }

    public void removeClientFromReservation(Long reservation_id, Long client_id) {
        var client = find.clientById(client_id);
        var reservation = find.reservationById(reservation_id);

        if (reservation != null && !reservation.getClient().isEmpty()) {
             reservation.getClient().remove(client);
             overnightStayReservationRepository.save(reservation);
        }
    }

    private void removeAllPaymentTypes(Long reservation_id, Long payment_id){
        var reservation = find.reservationById(reservation_id);
        var paymentType = find.paymentById(payment_id);

        if (reservation != null && !reservation.getClient().isEmpty()) {
            reservation.getPaymentType().remove(paymentType);
            overnightStayReservationRepository.save(reservation);
        }

    }
    private Float calculateTotal(OvernightStayReservation reservation){
        var period = Period.between(reservation.getStartDate(), reservation.getEndDate()).getDays();
        var amountPeople = reservation.getClient().size();
        var dailyValue = amountPeoplePrice(amountPeople);
        return dailyValue * period;
    }

    private ReservationResponse reservationResponse(OvernightStayReservation reservation){
        var total = calculateTotal(reservation);

        List<PaymentTypeResponse> paymentTypeList = new ArrayList<>();
        List<CustomerResponse> customerResponseList = new ArrayList<>();

        reservation.getPaymentType().forEach(type ->{
            PaymentTypeResponse response = new PaymentTypeResponse(type.getDescription());
            paymentTypeList.add(response);
        });

        if (reservation.getClient() != null) {
            reservation.getClient().forEach(client -> {
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
                        client.isHosted()
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
                reservation.getPaymentStatus(),
                customerResponseList.size(),
                reservation.getObs(),
                reservation.getActive(),
                total
                );
    }

    private void dateValidation(LocalDate startDate, LocalDate endDate){
        if (startDate.equals(endDate)) throw new EntityConflict("The dates entered cannot be on the same day");
        if (endDate.isBefore(startDate)) throw new EntityConflict("The date entered cannot be less than today");
    }

    public void isRoomAvailable(OvernightStayReservation reservation) {
        List<OvernightStayReservation> reservationsList = overnightStayReservationRepository.findAllByRoomAndActiveIsTrue(reservation.getRoom());

        for (OvernightStayReservation existingReservation : reservationsList) {
            if (isOverlap(existingReservation, reservation)) {
                throw new EntityConflict("The Room has busy on date entered");
            }
        }

    }
    private boolean isOverlap(OvernightStayReservation existingReservation, OvernightStayReservation newReservation) {
        return !(newReservation.getEndDate().isBefore(existingReservation.getStartDate()) ||
                newReservation.getStartDate().isAfter(existingReservation.getEndDate().minusDays(1)));
    }

    public void cancelReservation(Long reservation_id){
        var reservation = find.reservationById(reservation_id);
        reservation.setActive(false);
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
                reservation.getClient().forEach(client -> {
                    System.out.println("removed reservation of ID " + reservation.getId() + " of client" + client.getName());
                    removeClientFromReservation(reservation.getId(), client.getId());
                    reservation.getPaymentType().forEach(paymentType -> {
                        removeAllPaymentTypes(reservation.getId(), paymentType.getId());});
                });
            }
            overnightStayReservationRepository.deleteById(reservation.getId());
        });
    }
}
