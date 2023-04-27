package com.example.PousadaIstoE.services;

import com.example.PousadaIstoE.exceptions.EntityConflict;
import com.example.PousadaIstoE.exceptions.EntityNotFound;
import com.example.PousadaIstoE.model.Entradas;
import com.example.PousadaIstoE.model.MapaGeral;
import com.example.PousadaIstoE.model.Pernoites;
import com.example.PousadaIstoE.model.RegistroDeEntradas;
import com.example.PousadaIstoE.repository.EntradaRepository;
import com.example.PousadaIstoE.repository.MapaGeralRepository;
import com.example.PousadaIstoE.repository.PernoitesRepository;
import com.example.PousadaIstoE.repository.RegistroDeEntradasRepository;
import com.example.PousadaIstoE.response.EntradaResponse;
import com.example.PousadaIstoE.response.StatusPagamento;
import com.example.PousadaIstoE.response.TipoPagamento;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;

@Service
public class EntradaService {

    @PersistenceContext
    private EntityManager manager;
    double total;
    double valorEntrada;
    Duration diferenca;
    int horas;
    int minutosRestantes;
    private final EntradaRepository entradaRepository;
    private final PernoitesRepository pernoitesRepository;
    private final MapaGeralRepository mapaGeralRepository;
    private final RegistroDeEntradasRepository registroDeEntradasRepository;


    public EntradaService(EntradaRepository entradaRepository, PernoitesRepository pernoitesRepository, MapaGeralRepository mapaGeralRepository, RegistroDeEntradasRepository registroDeEntradasRepository) {
        this.entradaRepository = entradaRepository;
        this.pernoitesRepository = pernoitesRepository;
        this.mapaGeralRepository = mapaGeralRepository;
        this.registroDeEntradasRepository = registroDeEntradasRepository;
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
        entradas.setHoraSaida(LocalTime.of(0,0));
        validacaoDeApartamento(entradas);
        return entradaRepository.save(entradas);
    }

    public void updateEntradaData(Long entradaId, Entradas request) {

        final var entradas = entradaRepository.findById(entradaId).orElseThrow(() -> new EntityNotFound("Entrada não encontrada"));
        var entradaAtualizada = new Entradas();

        if (request.getHoraSaida() != entradas.getHoraSaida() || !Objects.equals(request.getConsumo(), entradas.getConsumo())) {
             entradaAtualizada = new Entradas(
                    entradas.getId(),
                    entradas.getApt(),
                    entradas.getHoraEntrada(),
                    LocalTime.now(),
                    request.getConsumo(),
                    entradas.getPlaca(),
                    request.getTipoPagamento(),
                    request.getStatus_pagamento()
            );
        }
        entradaRepository.save(entradaAtualizada);
        String relatorio;
        LocalTime noite = LocalTime.of(18,0,0);
        LocalTime dia = LocalTime.of(6,0,0);

        if (LocalTime.now().isAfter(noite) && LocalTime.now().isBefore(dia)){
            relatorio = "ENTRADA NOITE";
        } else {
            relatorio = "ENTRADA DIA";
        }
        calcularHora();

        if (request.getStatus_pagamento().equals(StatusPagamento.CONCLUIDO)){
            Float total = manager.createQuery("SELECT m.total FROM MapaGeral m ORDER BY m.id DESC", Float.class)
                    .setMaxResults(1)
                    .getSingleResult();

            double valorTotal = total + valorEntrada;
            MapaGeral mapaGeral = new MapaGeral(
            );
                mapaGeral.setApartment(entradas.getApt());
                mapaGeral.setData(LocalDate.now());
                mapaGeral.setEntrada((float) valorEntrada);
                mapaGeral.setReport(relatorio);
                mapaGeral.setTotal((float) valorTotal);
                mapaGeral.setSaida(0F);
                mapaGeral.setHora(LocalTime.now());

                if (request.getTipoPagamento().equals(TipoPagamento.PIX)){
                mapaGeral.setReport(relatorio + " (PIX)");
                mapaGeral.setEntrada(0F);
                mapaGeral.setTotal(total);
                }
                if (request.getTipoPagamento().equals(TipoPagamento.CARTAO)){
                mapaGeral.setReport(relatorio + " (CARTAO)");
                mapaGeral.setEntrada(0F);
                mapaGeral.setTotal(total);
            }
            RegistroDeEntradas registroDeEntradas = new RegistroDeEntradas(
            );
                registroDeEntradas.setId(entradaId);
                registroDeEntradas.setApt(entradas.getApt());
                registroDeEntradas.setHoraEntrada(entradas.getHoraEntrada());
                registroDeEntradas.setHoraSaida(entradas.getHoraSaida());
                registroDeEntradas.setConsumo(" ");
                registroDeEntradas.setPlaca(entradas.getPlaca());
                registroDeEntradas.setData(LocalDate.now());
                registroDeEntradas.setTipoPagamento(request.getTipoPagamento());
                registroDeEntradas.setStatus_pagamento(request.getStatus_pagamento());
                registroDeEntradas.setHoras(horas);
                registroDeEntradas.setMinutos(minutosRestantes);
                registroDeEntradas.setTotal(valorEntrada);

                registroDeEntradasRepository.save(registroDeEntradas);
                mapaGeralRepository.save(mapaGeral);
                entradaRepository.save(entradaAtualizada);
                entradaRepository.deleteById(entradaId);
        }
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
            }
        );
        valorEntrada = total;
    }
}