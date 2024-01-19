package com.example.PousadaIstoE.services;

import com.example.PousadaIstoE.exceptions.EntityConflict;
import com.example.PousadaIstoE.exceptions.EntityNotFound;
import com.example.PousadaIstoE.model.EntradaConsumption;
import com.example.PousadaIstoE.model.Entradas;
import com.example.PousadaIstoE.model.CashRegister;
import com.example.PousadaIstoE.model.Rooms;
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
    List<EntradaConsumption> entradaConsumptionList = new ArrayList<>();
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
        entradaConsumptionList = entradaConsumoRepository.findEntradaConsumoByEntradas_Id(id);
        final var entrada = entradaRepository.findById(id).orElseThrow(
                () -> new EntityNotFound("Entrada não foi Cadastrada ou não existe mais"));
        calcularHora(id);
        Double totalConsumo = entradaRepository.totalConsumo(id);

        if (totalConsumo == null){ totalConsumo = (double) 0; }
        double soma = totalConsumo + valorEntrada;

        List<ConsumptionResponse> consumptionResponseList = new ArrayList<>();
        entradaConsumptionList.forEach(consumo -> {
            ConsumptionResponse consumptionResponse = new ConsumptionResponse(
                    consumo.getQuantity(),
                    consumo.getItens().getDescription(),
                    consumo.getItens().getValue(),
                    consumo.getTotal()
            );
            consumptionResponseList.add(consumptionResponse);
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
                consumptionResponseList,
                entrada.getStatusEntrada(),
                totalConsumo,
                valorEntrada,
                soma
        ));
        return response;
    }

    public Entradas registerEntrada(Entradas entradas) {
        Rooms quartoOut = quartosFeing.findById(entradas.getQuartos().getId())
                .orElseThrow(()-> new EntityNotFound("Quarto não encontrado"));
        switch (quartoOut.getStatusDoQuarto()) {
            case OCUPADO -> throw new EntityConflict("Quarto Ocupado");
            case LIMPEZA -> throw new EntityConflict("Quarto Precisa de limpeza!");
            case RESERVADO -> throw new EntityConflict("Quarto Reservado!");
        }
        Entradas request = new Entradas(
            quartoOut,
            LocalTime.now(),
            LocalTime.of(0,0),
            entradas.getPlaca(),
            EntradaStatus.EM_ANDAMENTO,
            LocalDate.now(),
            PaymentType.PENDING,
            PaymentStatus.PENDENTE
        );
        quartoOut.setStatusDoQuarto(RoomStatus.BUSY);
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
        if (request.getTipoPagamento().equals(PaymentType.CASH)){
                entradaAtualizada.setTotal_entrada((float) entradaEConsumo);
            entradaRepository.save(entradaAtualizada);
        }
        if (request.getStatus_pagamento().equals(PaymentStatus.CONCLUIDO)) {
            if (entradaAtualizada.getStatusEntrada().equals(EntradaStatus.FINALIZADA)){
                throw new EntityConflict("A Entrada já foi salva no mapa");
            }
            entradaConsumptionList = entradaConsumoRepository.findEntradaConsumoByEntradas_Id(entradaId);
            entradaAtualizada.setEntradaConsumo(entradaConsumptionList);
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
        CashRegister cashRegister = new CashRegister(
                LocalDate.now(),
                relatorio,
                entradas.getQuartos().getNumero(),
                (float) entradaEConsumo,
                0F,
                (float) valorTotal,
                LocalTime.now()
        );
        switch (request.getTipoPagamento()){
            case PIX ->    { cashRegister.setReport(relatorio + " (PIX)");
                             cashRegister.setSaida(cashRegister.getEntrada()); }
            case CREDIT_CARD -> { cashRegister.setSaida(cashRegister.getEntrada());
                             cashRegister.setSaida(cashRegister.getEntrada()); }
            case CASH -> cashRegister.setReport(relatorio + " (DINHEIRO)");
        }
        mapaFeing.save(cashRegister);
    }

    private void consumoVazio(){
        var semConsumo = itensFeing.getItenVazio();
        EntradaConsumption entradaConsumption = new EntradaConsumption(
            0,
            semConsumo,
            entradas
        );
        entradaConsumoService.addConsumo(entradaConsumption);
    }

    public List<Entradas> findByStatusEntrada(EntradaStatus entradaStatus){
        return entradaRepository.findEntradasByStatusEntrada(entradaStatus);
    }

    public List<Entradas> findEntradaByToday(){
       LocalDate today = LocalDate.now();
       return entradaRepository.findEntradasByDataRegistroEntrada(today);
    }
    public List<Entradas> findEntradaByDate(LocalDate data){
        return entradaRepository.findEntradasByDataRegistroEntrada(data);
    }

    private void atualizaQuarto(Rooms rooms, Entradas entradaAtualizada){
        rooms = entradas.getQuartos();
        rooms.setStatusDoQuarto(RoomStatus.AVAIABLE);
        quartosFeing.save(rooms);
        entradaAtualizada.setStatusEntrada(EntradaStatus.FINALIZADA);
    }
}