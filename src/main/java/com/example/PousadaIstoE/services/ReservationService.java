package com.example.PousadaIstoE.services;

import com.example.PousadaIstoE.Enums.PaymentType;
import com.example.PousadaIstoE.builders.ReservationBuilder;
import com.example.PousadaIstoE.exceptions.EntityConflict;
import com.example.PousadaIstoE.model.Client;
import com.example.PousadaIstoE.model.DailyValues;
import com.example.PousadaIstoE.model.DailyValuesRepository;
import com.example.PousadaIstoE.model.OvernightStayReservation;
import com.example.PousadaIstoE.repository.ClientRepository;
import com.example.PousadaIstoE.repository.OvernightStayReservationRepository;
import com.example.PousadaIstoE.request.ReservationRequest;
import com.example.PousadaIstoE.request.UpdateReservationRequest;
import com.example.PousadaIstoE.response.ClientResponse;
import com.example.PousadaIstoE.response.ReservationResponse;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class ReservationService {
    private final OvernightStayReservationRepository overnightStayReservationRepository;
    private final ClientService clientService;
    private final Finder find;
    private final RoomService roomService;
    private final ClientRepository clientRepository;
    private final DailyValuesRepository dailyValuesRepository;

    public ReservationService(
            OvernightStayReservationRepository overnightStayReservationRepository,
            ClientService clientService,
            Finder find, RoomService roomService,
            ClientRepository clientRepository,
            DailyValuesRepository dailyValuesRepository) {
        this.overnightStayReservationRepository = overnightStayReservationRepository;
        this.clientService = clientService;
        this.find = find;
        this.roomService = roomService;
        this.clientRepository = clientRepository;
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
        List<Client> clientList = new ArrayList<>();
        dataValidation(request.start_date(), request.end_date());

        request.clients()
                .forEach(client -> {

                    var replacedCpf = find.replace(client.cpf());
                    Client findClient = clientRepository.findClientByCpf(replacedCpf);

                    if (findClient == null) {
                        findClient = clientService.clientBuilder(client);
                        clientList.add(findClient);
                        clientRepository.save(findClient);
                    } else {
                        clientList.add(findClient);
                    }
                });
        clientService.clientListVerification(clientList);

        OvernightStayReservation reservation = new ReservationBuilder()
                .startDate(request.start_date())
                .endDate(request.end_date())
                .room(request.room())
                .paymentType(request.payment_type())
                .paymentStatus(request.payment_status())
                .obs(request.obs().toUpperCase())
                .isActive(true)
                .build();
//        isRoomAvailable(reservation);
        reservation.setClient(clientList);
        overnightStayReservationRepository.save(reservation);
    }

    public void alterReservation(Long reservation_id, UpdateReservationRequest request){
        var reservation = find.reservationById(reservation_id);
        dataValidation(reservation.getStartDate(), reservation.getEndDate());

        List<Client> clientListUpdated = new ArrayList<>(reservation.getClient());

        if(!request.clients().isEmpty()) {
            request.clients().forEach(client -> {
                var replacedCpf = find.replace(client.cpf());
                Client findClient = clientRepository.findClientByCpf(replacedCpf);

                if (findClient == null) {
                    findClient = clientService.clientBuilder(client);
                    clientListUpdated.add(findClient);
                    clientRepository.save(findClient);
                } else {
                    clientListUpdated.add(findClient);
                }
            });
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
            .obs(request.obs()!= null ? request.obs() : "")
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
    private Float calculateTotal(OvernightStayReservation reservation){
        var period = Period.between(reservation.getStartDate(), reservation.getEndDate()).getDays();
        var amountPeople = reservation.getClient().size();
        var dailyValue = amountPeoplePrice(amountPeople);
        return dailyValue * period;
    }

    private ReservationResponse reservationResponse(OvernightStayReservation reservation){
        var total = calculateTotal(reservation);

        Set<PaymentType> paymentTypes = new HashSet<>(reservation.getPaymentType());
        List<ClientResponse> clientResponseList = new ArrayList<>();

        if (reservation.getClient() != null) {
            reservation.getClient().forEach(client -> {
                ClientResponse clientResponse = new ClientResponse(
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
                clientResponseList.add(clientResponse);
            });
        }
        return new ReservationResponse(
                reservation.getId(),
                clientResponseList,
                reservation.getStartDate(),
                reservation.getEndDate(),
                reservation.getRoom(),
                paymentTypes,
                reservation.getPaymentStatus(),
                clientResponseList.size(),
                reservation.getObs(),
                reservation.getActive(),
                total
                );
    }

    private void dataValidation(LocalDate startDate, LocalDate endDate){
        if (startDate.equals(endDate)) throw new EntityConflict("The dates entered cannot be on the same day");
        if (endDate.isBefore(startDate)) throw new EntityConflict("The date entered cannot be less than today");
    }

    public void isRoomAvailable(OvernightStayReservation reservation) {
        List<OvernightStayReservation> reservationsList = overnightStayReservationRepository.findAllByRoom(reservation.getRoom());

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
}
