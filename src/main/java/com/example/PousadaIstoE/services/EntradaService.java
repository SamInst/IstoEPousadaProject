package com.example.PousadaIstoE.services;

import com.example.PousadaIstoE.exceptions.EntityConflict;
import com.example.PousadaIstoE.exceptions.EntityNotFound;
import com.example.PousadaIstoE.model.EntradaConsumo;
import com.example.PousadaIstoE.model.Entradas;
import com.example.PousadaIstoE.model.MapaGeral;
import com.example.PousadaIstoE.model.Quartos;
import com.example.PousadaIstoE.repository.*;

import com.example.PousadaIstoE.response.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class EntradaService {
    private final MapaGeralRepository mapaFeing;
    private final QuartosRepository quartosFeing;
    private final ItensRepository itensFeing;
    private final EntradaConsumoService entradaConsumoService;
    double totalHorasEntrada;
    double valorEntrada;
    Duration diferenca;
    int horas;
    int minutosRestantes;
    String relatorio;
    double valorTotal;
    Entradas entradas;
    double entradaEConsumo;
    Float totalMapaGeral;
    List<EntradaConsumo> entradaConsumoList = new ArrayList<>();
    private final EntradaRepository entradaRepository;
    private final EntradaConsumoRepository entradaConsumoRepository;

    public EntradaService(MapaGeralRepository mapaFeing, QuartosRepository quartosFeing, ItensRepository itensFeing, EntradaConsumoService entradaConsumoService, EntradaRepository entradaRepository, EntradaConsumoRepository entradaConsumoRepository) {
        this.mapaFeing = mapaFeing;
        this.quartosFeing = quartosFeing;
        this.itensFeing = itensFeing;
        this.entradaConsumoService = entradaConsumoService;
        this.entradaRepository = entradaRepository;
        this.entradaConsumoRepository = entradaConsumoRepository;
    }


    public Page<EntradaSimplesResponse> findAll(Pageable pageable) {
        Page<Entradas> page = entradaRepository.findAll(pageable);

        List<EntradaSimplesResponse> entradaSimplesResponseList = new ArrayList<>();
        page.forEach(entradas -> {
            EntradaSimplesResponse entradaSimplesResponse = new EntradaSimplesResponse(
                    entradas.getId(),
                    entradas.getQuartos().getNumero(),
                    entradas.getHoraEntrada(),
                    entradas.getHoraSaida(),
                    entradas.getPlaca(),
                    entradas.getStatusEntrada()
            );
            entradaSimplesResponseList.add(entradaSimplesResponse);
        });
        return new PageImpl<>(entradaSimplesResponseList, pageable, page.getTotalElements());
    }

    public AtomicReference<EntradaResponse> findById(Long id) {
        AtomicReference<EntradaResponse> response = new AtomicReference<>();
        entradaConsumoList = entradaConsumoRepository.findEntradaConsumoByEntradas_Id(id);
        final var entrada = entradaRepository.findById(id).orElseThrow(
                () -> new EntityNotFound("Entrada não foi Cadastrada ou não existe mais"));
        calcularHora(id);
        Double totalConsumo = entradaRepository.totalConsumo(id);

        if (totalConsumo == null){ totalConsumo = (double) 0; }
        double soma = totalConsumo + valorEntrada;

        List<ConsumoResponse> consumoResponseList = new ArrayList<>();
        entradaConsumoList.forEach(consumo -> {
            ConsumoResponse consumoResponse = new ConsumoResponse(
                    consumo.getQuantidade(),
                    consumo.getItens().getDescricao(),
                    consumo.getItens().getValor(),
                    consumo.getTotal()
            );
            consumoResponseList.add(consumoResponse);
        });
        response.set(new EntradaResponse(
                entrada.getQuartos().getNumero(),
                entrada.getHoraEntrada(),
                entrada.getHoraSaida(),
                entrada.getPlaca(),
                new EntradaResponse.TempoPermanecido(
                        horas,
                        minutosRestantes
                ),
                consumoResponseList,
                entrada.getStatusEntrada(),
                totalConsumo,
                valorEntrada,
                soma
        ));
        return response;
    }

    public Entradas registerEntrada(Entradas entradas) {
        Quartos quartoOut = quartosFeing.findById(entradas.getQuartos().getId())
                .orElseThrow(()-> new EntityNotFound("Quarto não encontrado"));
        switch (quartoOut.getStatusDoQuarto()) {
            case OCUPADO -> throw new EntityConflict("Quarto Ocupado");
            case NECESSITA_LIMPEZA -> throw new EntityConflict("Quarto Precisa de limpeza!");
            case RESERVADO -> throw new EntityConflict("Quarto Reservado!");
        }
        Entradas request = new Entradas(
            quartoOut,
            LocalTime.now(),
            LocalTime.of(0,0),
            entradas.getPlaca(),
            StatusEntrada.EM_ANDAMENTO,
            LocalDate.now(),
            TipoPagamento.PENDENTE,
            StatusPagamento.PENDENTE
        );
        quartoOut.setStatusDoQuarto(StatusDoQuarto.OCUPADO);
        quartosFeing.save(quartoOut);
        return entradaRepository.save(request);
    }

    public void updateEntradaData(Long entradaId, Entradas request) {
        entradas = entradaRepository.findById(entradaId).orElseThrow(
                () -> new EntityNotFound("Entrada não encontrada")
        );
        var entradaAtualizada = new Entradas(
                entradas.getId(),
                entradas.getQuartos(),
                entradas.getHoraEntrada(),
                LocalTime.now(),
                entradas.getPlaca(),
                request.getTipoPagamento(),
                request.getStatus_pagamento(),
                entradas.getStatusEntrada()
        );
        entradaAtualizada.setDataRegistroEntrada(LocalDate.now());
        entradaRepository.save(entradaAtualizada
        );
        calcularHora(entradaAtualizada.getId());
        validacaoPagamento(entradas);
        if (request.getTipoPagamento().equals(TipoPagamento.DINHEIRO)){
                entradaAtualizada.setTotal_entrada((float) entradaEConsumo);
            entradaRepository.save(entradaAtualizada);
        }
        if (request.getStatus_pagamento().equals(StatusPagamento.CONCLUIDO)) {
            if (entradaAtualizada.getStatusEntrada().equals(StatusEntrada.FINALIZADA)){
                throw new EntityConflict("A Entrada já foi salva no mapa");
            }
            entradaConsumoList = entradaConsumoRepository.findEntradaConsumoByEntradas_Id(entradaId);
            entradaAtualizada.setEntradaConsumo(entradaConsumoList);
            if (entradaAtualizada.getEntradaConsumo().isEmpty()) { consumoVazio(); }

            validacaoHorario();
            salvaNoMapa(request);
            atualizaQuarto(entradas.getQuartos(), entradaAtualizada);
            entradaRepository.save(entradaAtualizada);
        }
    }

    private void calcularHora(Long request) {
        Entradas entrada = entradaRepository.findById(request)
            .orElseThrow(() -> new EntityNotFound("Entity Not found")
            );
        diferenca = Duration.between(entrada.getHoraEntrada(), entrada.getHoraSaida());
        long minutos = diferenca.toMinutes();
        horas = (int) (minutos / 60);
        minutosRestantes = (int) (minutos % 60);

        if (horas < 2) { totalHorasEntrada = 30.0; }
        else { int minutes = (int) ((((float)minutos - 120)/30)*5);
               totalHorasEntrada = 30 + minutes; }
        valorEntrada = totalHorasEntrada;
    }

    private void validacaoPagamento(Entradas request){
        totalMapaGeral = mapaFeing.findLastTotal();
        Double totalConsumo = entradaRepository.totalConsumo(request.getId());
        if (totalConsumo == null){ totalConsumo = 0D; }
        entradaEConsumo = valorEntrada + totalConsumo;
        valorTotal = totalMapaGeral + entradaEConsumo;
    }

    private void validacaoHorario(){
        LocalTime noite = LocalTime.of(18,0,0);
        LocalTime dia = LocalTime.of(6,0,0);
        relatorio = LocalTime.now().isAfter(noite) || LocalTime.now().isBefore(dia)
                ? "ENTRADA NOITE" : "ENTRADA DIA";
    }

    private void salvaNoMapa(Entradas request) {
        MapaGeral mapaGeral = new MapaGeral(
                LocalDate.now(),
                relatorio,
                entradas.getQuartos().getNumero(),
                (float) entradaEConsumo,
                0F,
                (float) valorTotal,
                LocalTime.now()
        );
        switch (request.getTipoPagamento()){
            case PIX ->    { mapaGeral.setReport(relatorio + " (PIX)");
                             mapaGeral.setSaida(mapaGeral.getEntrada()); }
            case CARTAO -> { mapaGeral.setSaida(mapaGeral.getEntrada());
                             mapaGeral.setSaida(mapaGeral.getEntrada()); }
            case DINHEIRO -> mapaGeral.setReport(relatorio + " (DINHEIRO)");
        }
        mapaFeing.save(mapaGeral);
    }

    private void consumoVazio(){
        var semConsumo = itensFeing.getItenVazio();
        EntradaConsumo entradaConsumo = new EntradaConsumo(
            0,
            semConsumo,
            entradas
        );
        entradaConsumoService.addConsumo(entradaConsumo);
    }

    public List<Entradas> findByStatusEntrada(StatusEntrada statusEntrada){
        return entradaRepository.findEntradasByStatusEntrada(statusEntrada);
    }

    public List<Entradas> findEntradaByToday(){
       LocalDate today = LocalDate.now();
       return entradaRepository.findEntradasByDataRegistroEntrada(today);
    }
    public List<Entradas> findEntradaByDate(LocalDate data){
        return entradaRepository.findEntradasByDataRegistroEntrada(data);
    }

    private void atualizaQuarto(Quartos quartos, Entradas entradaAtualizada){
        quartos = entradas.getQuartos();
        quartos.setStatusDoQuarto(StatusDoQuarto.DISPONIVEL);
        quartosFeing.save(quartos);
        entradaAtualizada.setStatusEntrada(StatusEntrada.FINALIZADA);
    }
}