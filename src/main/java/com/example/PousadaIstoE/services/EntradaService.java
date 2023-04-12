package com.example.PousadaIstoE.services;

import com.example.PousadaIstoE.exceptions.EntityConflict;
import com.example.PousadaIstoE.exceptions.EntityNotFound;
import com.example.PousadaIstoE.model.Entradas;
import com.example.PousadaIstoE.model.Pernoites;
import com.example.PousadaIstoE.repository.EntradaRepository;
import com.example.PousadaIstoE.repository.PernoitesRepository;
import com.example.PousadaIstoE.response.EntradaResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;

@Service
public class EntradaService {
    double total = 0.0;
    Duration diferenca;
    int horas;
    int minutosRestantes;
    private final EntradaRepository entradaRepository;
    private final PernoitesRepository pernoitesRepository;


    public EntradaService(EntradaRepository entradaRepository, PernoitesRepository pernoitesRepository) {
        this.entradaRepository = entradaRepository;

        this.pernoitesRepository = pernoitesRepository;
    }

    public List<Entradas> findAll() {
        return entradaRepository.findAll();
    }

    public ResponseEntity<EntradaResponse> findById(Long id) {
        final var entrada = entradaRepository.findById(id).orElseThrow(() -> new EntityNotFound("Entrada não foi Cadastrada"));
        calcularHora();
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

    public Entradas registerEntrada(Entradas entradas) {
        entradas.setHoraEntrada(LocalTime.now());

        validacaoDeApartamento(entradas);

        return entradaRepository.save(entradas);
    }

    public Entradas updateEntradaData(Long pernoiteId, Entradas request) {

        final var entradas = entradaRepository.findById(pernoiteId).orElseThrow(() -> new EntityNotFound("aqfszrgazs"));
        var entradaAtualizada = new Entradas();
        if (request.getHoraSaida() != entradas.getHoraSaida() || !Objects.equals(request.getConsumo(), entradas.getConsumo())) {
             entradaAtualizada = new Entradas(
                    entradas.getId(),
                    entradas.getApt(),
                    entradas.getHoraEntrada(),
                    request.getHoraSaida(),
                    request.getConsumo(),
                    entradas.getPlaca()
            );
        }
        return entradaRepository.save(entradaAtualizada);
    }

    private void validacaoDeApartamento(Entradas entradas) throws EntityConflict {

       List<Pernoites> pernoites = pernoitesRepository.findAll();
         pernoites.forEach(apartamento -> {
             List<Entradas> listaDeApartamentos = entradaRepository.findByApt(entradas.getApt());
             List<Pernoites> listaDeApartamentosPernoite = pernoitesRepository.findByApt(apartamento.getApt());
             for (Entradas entradaCadastrada : listaDeApartamentos) {
                 for (Pernoites pernoiteCadastrado : listaDeApartamentosPernoite) {
                     if ( entradas.getApt().equals(entradaCadastrada.getApt())
                       || entradas.getHoraSaida() == null ) {
//                             && apartamento.getApt().equals(entradas.getApt())
//                             && apartamento.getApt().equals(pernoiteCadastrado.getApt())
                         throw new EntityConflict("O apartamento está ocupado no momento.");
                     }
//                     if (pernoiteCadastrado.getApt().equals(entradaCadastrada.getApt()))
////                             && apartamento.getApt().equals(pernoiteCadastrado.getApt()))
//                     {
//                         throw new EntityConflict("O apartamento está ocupado no momento.");
//                     }
                 }
             }
         });
    }

    private void calcularHora(){
        List<Entradas> entradas = entradaRepository.findAll();
        entradas.forEach(entradas1 -> {
            diferenca = Duration.between(entradas1.getHoraEntrada(), entradas1.getHoraSaida());

            long minutos = diferenca.toMinutes();
            horas = (int) (minutos / 60);
            minutosRestantes = (int) (minutos % 60);

            if (horas < 2 || (horas == 2 && minutosRestantes <= 20)) {
                total = 30.0;
            } else {
                total = 30.0 + ((horas - 2) * 7.0);
                if (minutosRestantes > 0) {
                    total += 7.0;
                }
            }
        });
    }
}