package com.example.PousadaIstoE.services;

import com.example.PousadaIstoE.exceptions.EntityConflict;
import com.example.PousadaIstoE.exceptions.EntityNotFound;
import com.example.PousadaIstoE.model.Entradas;
import com.example.PousadaIstoE.model.MapaGeral;
import com.example.PousadaIstoE.model.Pernoites;
import com.example.PousadaIstoE.model.RegistroDeEntradas;
import com.example.PousadaIstoE.repository.RegistroDeEntradasRepository;
import com.example.PousadaIstoE.response.EntradaResponse;
import com.example.PousadaIstoE.response.StatusPagamento;
import com.example.PousadaIstoE.response.TipoPagamento;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;

@Service
public class RegistroDeEntradasService {
    LocalTime hora_saida = LocalTime.now();

    double total;
    double valorEntrada;
    Duration diferenca;
    int horas;
    int minutosRestantes;
    private final RegistroDeEntradasRepository registroDeEntradasRepository;

    public RegistroDeEntradasService(RegistroDeEntradasRepository registroDeEntradasRepository) {
        this.registroDeEntradasRepository = registroDeEntradasRepository;
    }

    public List<RegistroDeEntradas> findAll() {
        return registroDeEntradasRepository.findAll();
    }

    public ResponseEntity<EntradaResponse> findById(Long id) {
        final var entrada = registroDeEntradasRepository.findById(id).orElseThrow(() -> new EntityNotFound("Registro não Encontrado"));

        final var response =
                new EntradaResponse(
                entrada.getApt(),
                entrada.getHoraEntrada(),
                entrada.getHoraSaida(),
                entrada.getConsumo(),
                entrada.getPlaca(),
                new EntradaResponse.TempoPermanecido(
                horas,
                minutosRestantes
                ),
                total
        );
        return ResponseEntity.ok(response);
    }
}