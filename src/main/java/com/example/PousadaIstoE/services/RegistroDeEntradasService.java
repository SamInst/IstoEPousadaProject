package com.example.PousadaIstoE.services;

import com.example.PousadaIstoE.exceptions.EntityNotFound;
import com.example.PousadaIstoE.model.*;
import com.example.PousadaIstoE.repository.EntradaConsumoRepository;
import com.example.PousadaIstoE.repository.RegistroDeEntradasRepository;
import com.example.PousadaIstoE.response.EntradaResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RegistroDeEntradasService {
    double total;
    int horas;
    int minutosRestantes;
    private final RegistroDeEntradasRepository registroDeEntradasRepository;
    private final EntradaConsumoRepository entradaConsumoRepository;

    public RegistroDeEntradasService(RegistroDeEntradasRepository registroDeEntradasRepository, EntradaConsumoRepository entradaConsumoRepository) {
        this.registroDeEntradasRepository = registroDeEntradasRepository;
        this.entradaConsumoRepository = entradaConsumoRepository;
    }

    public List<RegistroDeEntradas> findAll() {
        return registroDeEntradasRepository.findAll();
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