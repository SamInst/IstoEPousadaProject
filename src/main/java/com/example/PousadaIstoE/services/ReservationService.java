package com.example.PousadaIstoE.services;

import com.example.PousadaIstoE.builders.ReservationBuilder;
import com.example.PousadaIstoE.exceptions.EntityConflict;
import com.example.PousadaIstoE.model.Client;
import com.example.PousadaIstoE.model.OvernightStayReservation;
import com.example.PousadaIstoE.repository.OvernightStayReservationRepository;
import com.example.PousadaIstoE.request.ReservationRequest;
import com.example.PousadaIstoE.request.UpdateReservationRequest;
import com.example.PousadaIstoE.response.ClientResponse;
import com.example.PousadaIstoE.response.ReservationResponse;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class ReservationService {
    private final OvernightStayReservationRepository overnightStayReservationRepository;
    private final ClientService clientService;
    private final Finder find;
    private final RoomService roomService;

    public ReservationService(
            OvernightStayReservationRepository overnightStayReservationRepository,
            ClientService clientService,
            Finder find, RoomService roomService) {
        this.overnightStayReservationRepository = overnightStayReservationRepository;
        this.clientService = clientService;
        this.find = find;
        this.roomService = roomService;
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
        List<Client> clientList = new ArrayList<>();
        dataValidation(request.start_date(), request.end_date());
        var room = find.roomByNumber(request.room());
        roomService.roomVerification(room);

        request.clients()
                .forEach(clientService::clientRequest);

        clientService.clientListVerification(clientList);
        OvernightStayReservation reservation = new ReservationBuilder()
                .startDate(request.start_date())
                .endDate(request.end_date())
                .clientList(clientList)
                .room(request.room())
                .paymentType(request.payment_type())
                .paymentStatus(request.payment_status())
                .isActive(true)
                .build();
        overnightStayReservationRepository.save(reservation);
    }

    public void alterReservation(Long reservation_id, UpdateReservationRequest request){
        var reservation = find.reservationById(reservation_id);
        dataValidation(reservation.getStartDate(), reservation.getEndDate());

        List<Client> clientListUpdated = new ArrayList<>(reservation.getClientList());

        if(!request.clients().isEmpty()) {
            request.clients().forEach(clientService::clientRequest);
        }
        clientService.clientListVerification(clientListUpdated);

        OvernightStayReservation updateReservation = new ReservationBuilder()
            .id(reservation.getId())
            .startDate(request.start_date())
            .endDate(request.end_date())
            .clientList(clientListUpdated)
            .room(request.room())
            .paymentType(request.payment_type())
            .paymentStatus(request.payment_status())
            .isActive(reservation.getActive())
            .build();
        overnightStayReservationRepository.save(updateReservation);
    }

    public void removeClientFromReservation(Long reservation_id, Long client_id) {
        var client = find.clientById(client_id);
        var reservation = find.reservationById(reservation_id);

        if (reservation != null && !reservation.getClientList().isEmpty()) {
             reservation.getClientList().remove(client);
             overnightStayReservationRepository.save(reservation);
        }
    }

    private ReservationResponse reservationResponse(OvernightStayReservation reservation){
        List<ClientResponse> clientResponseList = new ArrayList<>();

        if (reservation.getClientList() != null) {
            reservation.getClientList().forEach(client -> {
                ClientResponse clientResponse = new ClientResponse(
                        client.getId(),
                        client.getName(),
                        client.getCpf(),
                        client.getPhone(),
                        client.getAddress(),
                        client.getJob(),
                        client.getRegisteredBy() != null ? client.getRegisteredBy().getName() : "",
                        client.isHosted()
                );
                clientResponseList.add(clientResponse);
            });
        }
        return new ReservationResponse(
                reservation.getId(),
                clientResponseList,
                reservation.getStartDate(),
                reservation.getEndDate(),
                reservation.getRoom(),
                reservation.getPaymentType(),
                reservation.getPaymentStatus(),
                clientResponseList.size()
                );
    }

    private void dataValidation(LocalDate startDate, LocalDate endDate){
        if (startDate.equals(endDate)) throw new EntityConflict("The dates entered cannot be on the same day");
        if (endDate.isBefore(startDate)) throw new EntityConflict("The date entered cannot be less than today");
    }

    public void cancelReservation(Long reservation_id){
        var reservation = find.reservationById(reservation_id);
        reservation.setActive(false);
        overnightStayReservationRepository.save(reservation);
    }
}
