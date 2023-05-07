package com.example.PousadaIstoE.services;

import com.example.PousadaIstoE.model.*;
import com.example.PousadaIstoE.repository.RegistroDeEntradasRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class RegistroDeEntradasService {
    private final RegistroDeEntradasRepository registroDeEntradasRepository;


    public RegistroDeEntradasService(RegistroDeEntradasRepository registroDeEntradasRepository) {
        this.registroDeEntradasRepository = registroDeEntradasRepository;

    }

    public List<RegistroDeEntradas> findAll() {
        return registroDeEntradasRepository.findAll();
    }

    public List<RegistroDeEntradas> findByData(LocalDate localDate){
        return registroDeEntradasRepository.findByData(localDate);
    }

//    public ResponseEntity<EntradaResponse> findById(Long id) {
//        final var entrada = registroDeEntradasRepository.findById(id).orElseThrow(() -> new EntityNotFound("Registro não Encontrado"));
//        final var response =
//                new EntradaResponse(
//                entrada.getApt(),
//                entrada.getHoraEntrada(),
//                entrada.getHoraSaida(),
//                entrada.getPlaca(),
//                new EntradaResponse.TempoPermanecido(
//                horas,
//                minutosRestantes
//                ),
//
//                total
//        );
//        return ResponseEntity.ok(response);
//    }
}