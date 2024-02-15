package com.example.PousadaIstoE.services;

import com.example.PousadaIstoE.Enums.PaymentStatus;
import com.example.PousadaIstoE.builders.ClientBuilder;
import com.example.PousadaIstoE.builders.ReservationBuilder;
import com.example.PousadaIstoE.exceptions.EntityConflict;
import com.example.PousadaIstoE.model.Client;
import com.example.PousadaIstoE.model.OvernightStayReservation;
import com.example.PousadaIstoE.repository.ClientRepository;
import com.example.PousadaIstoE.repository.OvernightStayReservationRepository;
import com.example.PousadaIstoE.request.ClientRequest;
import com.example.PousadaIstoE.request.ReservationRequest;
import com.example.PousadaIstoE.request.UpdateReservationRequest;
import com.example.PousadaIstoE.response.ClientResponse;
import com.example.PousadaIstoE.response.ReservationResponse;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class ReservationService {
    private static final String NE = "Not Specified";
    private final OvernightStayReservationRepository overnightStayReservationRepository;
    private final ClientRepository clientRepository;
    private final Finder find;

    public ReservationService(
            OvernightStayReservationRepository overnightStayReservationRepository,
            ClientRepository clientRepository,
            Finder find) {
        this.overnightStayReservationRepository = overnightStayReservationRepository;
        this.clientRepository = clientRepository;
        this.find = find;
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
        dataValidation(request.startDate(), request.endDate());

        request.clients().forEach(client -> {
            var replacedCpf = replace(client.cpf());
            Client findClient = clientRepository.findClientByCpf(replacedCpf);

            if (findClient == null) {
                findClient = clientBuilder(client);
                clientList.add(findClient);
                clientRepository.save(findClient);
            } else {
                clientList.add(findClient);
            }
        });
        Set<Client> uniqueClients = new HashSet<>(clientList);
        if (uniqueClients.size() < clientList.size()) {
            throw new EntityConflict("The customer has already been added to this list.");
        }
        OvernightStayReservation reservation = new ReservationBuilder()
                .startDate(request.startDate())
                .endDate(request.endDate())
                .clientList(clientList)
                .room(request.room())
                .paymentType(request.paymentType())
                .paymentStatus(PaymentStatus.PENDING)
                .isActive(true)
                .build();
        overnightStayReservationRepository.save(reservation);
    }

    public void alterReservation(Long reservation_id, UpdateReservationRequest request){
        var reservation = find.reservationById(reservation_id);
        dataValidation(reservation.getStartDate(), reservation.getEndDate());

        List<Client> clientListUpdated = new ArrayList<>(reservation.getClientList());

        if(!request.clients().isEmpty()) {
            request.clients().forEach(client -> {
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
            .isActive(reservation.getActive())
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
        if (startDate.equals(endDate)) throw new EntityConflict("The dates entered cannot be on the same day");
        if (endDate.isBefore(startDate)) throw new EntityConflict("The date entered cannot be less than today");
    }

    public void cancelReservation(Long reservation_id){
        var reservation = find.reservationById(reservation_id);
        reservation.setActive(false);
        overnightStayReservationRepository.save(reservation);
    }
}
