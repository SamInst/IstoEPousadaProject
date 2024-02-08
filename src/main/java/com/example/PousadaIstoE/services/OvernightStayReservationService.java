package com.example.PousadaIstoE.services;

import com.example.PousadaIstoE.Enums.PaymentStatus;
import com.example.PousadaIstoE.builders.ClientBuilder;
import com.example.PousadaIstoE.builders.ReservationBuilder;
import com.example.PousadaIstoE.exceptions.EntityConflict;
import com.example.PousadaIstoE.model.Client;
import com.example.PousadaIstoE.model.OvernightStayReservation;
import com.example.PousadaIstoE.repository.ClientRepository;
import com.example.PousadaIstoE.repository.OvernightStayCompanionRepository;
import com.example.PousadaIstoE.repository.OvernightStayReservationRepository;
import com.example.PousadaIstoE.request.ClientRequest;
import com.example.PousadaIstoE.request.ReservationRequest;
import com.example.PousadaIstoE.request.UpdateReservationRequest;
import com.example.PousadaIstoE.response.ClientResponse;
import com.example.PousadaIstoE.response.ReservationResponse;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class OvernightStayReservationService {
    private static final String NE = "Not Specified";
    private final OvernightStayReservationRepository overnightStayReservationRepository;
    private final OvernightStayCompanionRepository overnightStayCompanionRepository;
    private final ClientRepository clientRepository;
    private final Finder find;

    public OvernightStayReservationService(
            OvernightStayReservationRepository overnightStayReservationRepository,
            OvernightStayCompanionRepository overnightStayCompanionRepository,
            ClientRepository clientRepository,
            Finder find) {
        this.overnightStayReservationRepository = overnightStayReservationRepository;
        this.overnightStayCompanionRepository = overnightStayCompanionRepository;
        this.clientRepository = clientRepository;
        this.find = find;
    }

    public ReservationResponse findReservationById(Long reservation_id){
        var reservation = find.reservationById(reservation_id);
        return reservationResponse(reservation);
    }

    public List<ReservationResponse> allReservationsByDate(LocalDate date) {
        List<ReservationResponse> reservationResponseList = new ArrayList<>();
        var reservations = overnightStayReservationRepository.findAllByStartDate(date);

        for (OvernightStayReservation reservation : reservations) {
            reservationResponseList.add(reservationResponse(reservation));
        }
        return reservationResponseList;
    }


    public void createReservation(ReservationRequest request) {
        List<Client> clientList = new ArrayList<>();

        request.clients().forEach(client -> {
            Client findClient = clientRepository.findClientByCpf(client.cpf());

            if (request.clients().contains(client)) throw new EntityConflict("The customer has already been added to this list.");

            if (findClient == null) {
                findClient = clientBuilder(client);
                Client newClient = clientRepository.save(findClient);
                clientList.add(newClient);

            } else clientList.add(findClient);
        });
        OvernightStayReservation reservation = new ReservationBuilder()
                .startDate(request.startDate())
                .endDate(request.endDate())
                .clientList(clientList)
                .room(request.room())
                .paymentType(request.paymentType())
                .paymentStatus(PaymentStatus.PENDING)
                .build();
        overnightStayReservationRepository.save(reservation);
    }


    public void alterReservation(Long reservation_id, UpdateReservationRequest request){
        var reservation = find.reservationById(reservation_id);

        List<Client> clientListUpdated = new ArrayList<>(reservation.getClientList());

        if(!request.clientList().isEmpty()) {
            request.clientList().forEach(client -> {
                var findClient = clientRepository.findClientByCpf(replace(client.cpf()));
                if (clientListUpdated.contains(findClient))
                    throw new EntityConflict("The customer has already been added to this list.");
                if (findClient == null) {
                    findClient = clientBuilder(client);
                    Client newClient = clientRepository.save(findClient);
                    clientListUpdated.add(newClient);
                } else {
                    clientListUpdated.add(findClient);
                }
            });
        }
        OvernightStayReservation updateReservation = new ReservationBuilder()
            .id(reservation.getId())
            .startDate(request.startDate())
            .endDate(request.endDate())
            .clientList(clientListUpdated)
            .room(request.room())
            .paymentType(request.paymentType())
            .paymentStatus(reservation.getPaymentStatus())
            .build();
        overnightStayReservationRepository.save(updateReservation);
    }

    private Client clientBuilder(ClientRequest client){
        return new ClientBuilder()
                .name(client.name() != null ? replace(client.name()) : NE)
                .cpf(client.cpf() != null ? replace(client.cpf()) : NE)
                .phone(client.phone() != null ? replace(client.phone()) : NE)
                .birth(client.birth())
                .address(client.address() != null ? replace(client.address()) : NE)
                .job(client.job() != null ? replace(client.job()) : NE)
                .isHosted(false)
                .build();
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

    public static String replace(String string) {
        String regex = "[^a-zA-Z0-9\\s]";
        String newString = string.replaceAll(regex, "");
        return newString.toUpperCase();
    }


    private void dataValidation(LocalDate startDate, LocalDate endDate){
        if (startDate.equals(endDate)) throw new EntityConflict("The data inserted cannot be in the same day");
        if (endDate.isBefore(startDate)) throw new EntityConflict("The data inserted cannot be less than today");
    }
}
