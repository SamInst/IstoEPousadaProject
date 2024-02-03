package com.example.PousadaIstoE.services;

import com.example.PousadaIstoE.builders.ClientBuilder;
import com.example.PousadaIstoE.builders.CompanionBuilder;
import com.example.PousadaIstoE.builders.ReservationBuilder;
import com.example.PousadaIstoE.model.Client;
import com.example.PousadaIstoE.model.OvernightStayCompanion;
import com.example.PousadaIstoE.model.OvernightStayReservation;
import com.example.PousadaIstoE.repository.ClientRepository;
import com.example.PousadaIstoE.repository.OvernightStayCompanionRepository;
import com.example.PousadaIstoE.repository.OvernightStayReservationRepository;
import com.example.PousadaIstoE.request.CompanionRequest;
import com.example.PousadaIstoE.request.ReservationRequest;
import com.example.PousadaIstoE.request.UpdateReservationRequest;
import com.example.PousadaIstoE.response.ClientResponse;
import com.example.PousadaIstoE.response.CompanionResponse;
import com.example.PousadaIstoE.response.ReservationResponse;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

@Service
public class OvernightStayReservationService {
    private static final String NE = "NÃO ESPECIFICADO";
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

    public void createReservation(Long client_id, ReservationRequest request){
        var findClient = find.clientById(client_id);
        Client client = null;

        List<OvernightStayCompanion> companionList = new ArrayList<>();

        if (findClient == null){
             client = new ClientBuilder()
                    .name(request.client().name() != null ? request.client().name() : NE)
                    .cpf(request.client().cpf() != null ? request.client().cpf() : NE)
                    .phone(request.client().phone() != null ? request.client().phone() : NE)
                    .birth(request.client().birth())
                    .address(request.client().address() != null ? request.client().address() : NE)
                    .job(request.client().job() != null ? request.client().job() : NE)
                    .active(true)
                    .build();
        }
        assert client != null;
        var newClient = clientRepository.save(client);

        if (!request.companions().isEmpty()){
             request.companions().forEach(newCompanion -> {

                 OvernightStayCompanion companion = new CompanionBuilder()
                        .name(newCompanion.name())
                        .cpf(newCompanion.cpf())
                        .birth(newCompanion.birth())
                        .client(newClient)
                        .build();
                 companionList.add(companion);
                overnightStayCompanionRepository.save(companion);
            });
        }
        OvernightStayReservation reservation = new ReservationBuilder()
                .startDate(request.startDate())
                .endDate(request.endDate())
                .client(newClient)
                .companions(companionList)
                .room(request.room())
                .paymentType(request.paymentType())
                .build();
        overnightStayReservationRepository.save(reservation);
    }

    public void alterReservation(Long reservation_id, UpdateReservationRequest request){
        var reservation = find.reservationById(reservation_id);
        List<OvernightStayCompanion> companionList = new ArrayList<>(reservation.getCompanion());

        if (!request.companions().isEmpty()){
                request.companions().forEach(newCompanion -> {

                    OvernightStayCompanion companion = new CompanionBuilder()
                            .name(newCompanion.name())
                            .cpf(newCompanion.cpf())
                            .birth(newCompanion.birth())
                            .client(reservation.getClient())
                            .build();
                    companionList.add(companion);
                    overnightStayCompanionRepository.save(companion);
            });
        }
        OvernightStayReservation updateReservation = new ReservationBuilder()
            .id(reservation.getId())
            .startDate(request.startDate())
            .endDate(request.endDate())
            .client(reservation.getClient())
            .companions(companionList)
            .room(request.room())
            .paymentType(request.paymentType())
            .build();
        overnightStayReservationRepository.save(updateReservation);
    }

    public void removeCompanion(Long companion_id){
        var companion = find.companionById(companion_id);
        overnightStayCompanionRepository.delete(companion);
    }

    private ReservationResponse reservationResponse(OvernightStayReservation reservation){
        List<CompanionResponse> companionResponseList = new ArrayList<>();

        if (!reservation.getCompanion().isEmpty()){
            reservation.getCompanion().forEach(companion -> {
                var ageCompanion = Period.between(companion.getBirth(), LocalDate.now());
                CompanionResponse companionResponse = new CompanionResponse(
                        companion.getId(),
                        companion.getName() != null ? companion.getName() : NE,
                        companion.getCpf()  != null ? companion.getCpf() : NE,
                        companion.getBirth(),
                        ageCompanion.getYears()
                );
                companionResponseList.add(companionResponse);
            });
        }
        return new ReservationResponse(reservation.getId(),
                new ClientResponse(
                        reservation.getClient().getId(),
                        reservation.getClient().getName() != null ? reservation.getClient().getName() : NE,
                        reservation.getClient().getCpf()!= null ? reservation.getClient().getCpf() : NE,
                        reservation.getClient().getPhone() != null ? reservation.getClient().getPhone() : NE,
                        reservation.getClient().getAddress() != null ? reservation.getClient().getAddress() : NE,
                        reservation.getClient().getJob() != null ? reservation.getClient().getJob() : NE,
                        reservation.getClient().getRegisteredBy() != null ? reservation.getClient().getRegisteredBy().getName() : NE,
                        reservation.getClient().isActive()),
                companionResponseList,
                reservation.getStartDate(),
                reservation.getEndDate(),
                reservation.getRoom(),
                reservation.getPaymentType()
                );
    }

}
