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
import com.example.PousadaIstoE.request.ReservationRequest;
import org.springframework.stereotype.Service;

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

    public void createReservation(ReservationRequest request){

        List<OvernightStayCompanion> companionList = new ArrayList<>();

        Client client = new ClientBuilder()
                .name(request.client().name() != null ? request.client().name() : NE)
                .cpf(request.client().cpf() != null ? request.client().cpf() : NE)
                .phone(request.client().phone()!= null ? request.client().phone() : NE)
                .birth(request.client().birth())
                .address(request.client().address()!= null ? request.client().address() : NE)
                .job(request.client().job() != null ? request.client().job() : NE)
                .build();
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
                .build();
        overnightStayReservationRepository.save(reservation);
    }
}
