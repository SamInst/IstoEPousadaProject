package com.example.PousadaIstoE.services;

import com.example.PousadaIstoE.exceptions.EntityNotFound;
import com.example.PousadaIstoE.model.Entradas;
import com.example.PousadaIstoE.model.Pernoites;
import com.example.PousadaIstoE.repository.EntradaRepository;
import com.example.PousadaIstoE.response.EntradaResponse;
import org.springframework.beans.BeanUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;


@Service
public class EntradaService {

    private final EntradaRepository entradaRepository;

    public EntradaService(EntradaRepository entradaRepository) {
        this.entradaRepository = entradaRepository;
    }

    public List<Entradas> findAll() {
        return entradaRepository.findAll();
    }

    public ResponseEntity<EntradaResponse> findById(Long id) {
        final var entrada = entradaRepository.findById(id).orElseThrow(() -> new EntityNotFound("Entrada não foi Cadastrada"));
        Float total = null;
        final var response = new EntradaResponse(
                entrada.getApt(),
                entrada.getHoraEntrada(),
                entrada.getHoraSaida(),
                entrada.getConsumo(),
                entrada.getPlaca(),
                total
        );
        return ResponseEntity.ok(response);
    }

    public Entradas registerEntrada(Entradas entradas) {
        entradas.setHoraEntrada(LocalTime.now());
        return entradaRepository.save(entradas);
    }

    public Entradas updateEntradaData(Long pernoiteId, Entradas request) {
        final var entradas = entradaRepository.findById(pernoiteId).orElseThrow(() -> new EntityNotFound("aqfszrgazs"));
        var entradaAtualizada = new Entradas();
       //comparar request com entradas
        if (request.getHoraSaida() != entradas.getHoraSaida()) {
             entradaAtualizada = new Entradas(
                    entradas.getId(),
                    entradas.getApt(),
                    entradas.getHoraEntrada(),
                    request.getHoraSaida(),
                    entradas.getConsumo(),
                    entradas.getPlaca()

            );
        }
        if (!request.getConsumo().equals(entradas.getConsumo())) {
             entradaAtualizada = new Entradas(
                    entradas.getId(),
                    entradas.getApt(),
                    entradas.getHoraEntrada(),
                    entradas.getHoraSaida(),
                    request.getConsumo(),
                    entradas.getPlaca()

            );
        }
        return entradaRepository.save(entradaAtualizada);
    }
}

