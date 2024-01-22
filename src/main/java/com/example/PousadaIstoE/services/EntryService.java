package com.example.PousadaIstoE.services;

import com.example.PousadaIstoE.Enums.EntryStatus;
import com.example.PousadaIstoE.Enums.PaymentStatus;
import com.example.PousadaIstoE.Enums.PaymentType;
import com.example.PousadaIstoE.Enums.RoomStatus;
import com.example.PousadaIstoE.exceptions.EntityConflict;
import com.example.PousadaIstoE.exceptions.EntityNotFound;
import com.example.PousadaIstoE.model.EntryConsumption;
import com.example.PousadaIstoE.model.Entry;
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
public class EntryService {
    private final CashRegisterRepository mapaFeing;
    private final RoomRepository quartosFeing;
    private final ItemRepository itensFeing;
    private final EntryConsumptionService entryConsumptionService;
    double totalHorasEntrada;
    double valorEntrada;
    Duration diferenca;
    int horas;
    int minutosRestantes;
    String relatorio;
    double valorTotal;
    Entry entry;
    double entradaEConsumo;
    Float totalMapaGeral;
    List<EntryConsumption> entryConsumptionList = new ArrayList<>();
    private final EntryRepository entryRepository;
    private final EntryConsumptionRepository entryConsumptionRepository;

    public EntryService(CashRegisterRepository mapaFeing, RoomRepository quartosFeing, ItemRepository itensFeing, EntryConsumptionService entryConsumptionService, EntryRepository entryRepository, EntryConsumptionRepository entryConsumptionRepository) {
        this.mapaFeing = mapaFeing;
        this.quartosFeing = quartosFeing;
        this.itensFeing = itensFeing;
        this.entryConsumptionService = entryConsumptionService;
        this.entryRepository = entryRepository;
        this.entryConsumptionRepository = entryConsumptionRepository;
    }


    public Page<SimpleEntryResponse> findAll(Pageable pageable) {
        Page<Entry> page = entryRepository.findAll(pageable);

        List<SimpleEntryResponse> simpleEntryResponseList = new ArrayList<>();
        page.forEach(entradas -> {
            SimpleEntryResponse simpleEntryResponse = new SimpleEntryResponse(
                    entradas.getId(),
                    entradas.getQuartos().getNumero(),
                    entradas.getHoraEntrada(),
                    entradas.getHoraSaida(),
                    entradas.getPlaca(),
                    entradas.getStatusEntrada()
            );
            simpleEntryResponseList.add(simpleEntryResponse);
        });
        return new PageImpl<>(simpleEntryResponseList, pageable, page.getTotalElements());
    }

    public AtomicReference<EntryResponse> findById(Long id) {
        AtomicReference<EntryResponse> response = new AtomicReference<>();
        entryConsumptionList = entryConsumptionRepository.findEntradaConsumoByEntradas_Id(id);
        final var entrada = entryRepository.findById(id).orElseThrow(
                () -> new EntityNotFound("Entrada não foi Cadastrada ou não existe mais"));
        calcularHora(id);
        Double totalConsumo = entryRepository.totalConsumo(id);

        if (totalConsumo == null){ totalConsumo = (double) 0; }
        double soma = totalConsumo + valorEntrada;

        List<ConsumptionResponse> consumptionResponseList = new ArrayList<>();
        entryConsumptionList.forEach(consumo -> {
            ConsumptionResponse consumptionResponse = new ConsumptionResponse(
                    consumo.getQuantity(),
                    consumo.getItens().getDescription(),
                    consumo.getItens().getValue(),
                    consumo.getTotal()
            );
            consumptionResponseList.add(consumptionResponse);
        });
        response.set(new EntryResponse(
                entrada.getQuartos().getNumero(),
                entrada.getHoraEntrada(),
                entrada.getHoraSaida(),
                entrada.getPlaca(),
                new EntryResponse.TempoPermanecido(
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

    public Entry registerEntrada(Entry entry) {
        Rooms quartoOut = quartosFeing.findById(entry.getQuartos().getId())
                .orElseThrow(()-> new EntityNotFound("Quarto não encontrado"));
        switch (quartoOut.getStatusDoQuarto()) {
            case OCUPADO -> throw new EntityConflict("Quarto Ocupado");
            case LIMPEZA -> throw new EntityConflict("Quarto Precisa de limpeza!");
            case RESERVADO -> throw new EntityConflict("Quarto Reservado!");
        }
        Entry request = new Entry(
            quartoOut,
            LocalTime.now(),
            LocalTime.of(0,0),
            entry.getPlaca(),
            EntryStatus.IN_PROGRESS,
            LocalDate.now(),
            PaymentType.PENDING,
            PaymentStatus.PENDENTE
        );
        quartoOut.setStatusDoQuarto(RoomStatus.BUSY);
        quartosFeing.save(quartoOut);
        return entryRepository.save(request);
    }

    public void updateEntradaData(Long entradaId, Entry request) {
        entry = entryRepository.findById(entradaId).orElseThrow(
                () -> new EntityNotFound("Entrada não encontrada")
        );
        var entradaAtualizada = new Entry(
                entry.getId(),
                entry.getQuartos(),
                entry.getHoraEntrada(),
                LocalTime.now(),
                entry.getPlaca(),
                request.getTipoPagamento(),
                request.getStatus_pagamento(),
                entry.getStatusEntrada()
        );
        entradaAtualizada.setDataRegistroEntrada(LocalDate.now());
        entryRepository.save(entradaAtualizada
        );
        calcularHora(entradaAtualizada.getId());
        validacaoPagamento(entry);
        if (request.getTipoPagamento().equals(PaymentType.CASH)){
                entradaAtualizada.setTotal_entrada((float) entradaEConsumo);
            entryRepository.save(entradaAtualizada);
        }
        if (request.getStatus_pagamento().equals(PaymentStatus.CONCLUIDO)) {
            if (entradaAtualizada.getStatusEntrada().equals(EntryStatus.FINISH)){
                throw new EntityConflict("A Entrada já foi salva no mapa");
            }
            entryConsumptionList = entryConsumptionRepository.findEntradaConsumoByEntradas_Id(entradaId);
            entradaAtualizada.setEntradaConsumo(entryConsumptionList);
            if (entradaAtualizada.getEntradaConsumo().isEmpty()) { consumoVazio(); }

            validacaoHorario();
            salvaNoMapa(request);
            atualizaQuarto(entry.getQuartos(), entradaAtualizada);
            entryRepository.save(entradaAtualizada);
        }
    }

    private void calcularHora(Long request) {
        Entry entrada = entryRepository.findById(request)
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

    private void validacaoPagamento(Entry request){
        totalMapaGeral = mapaFeing.findLastTotal();
        Double totalConsumo = entryRepository.totalConsumo(request.getId());
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

    private void salvaNoMapa(Entry request) {
        CashRegister cashRegister = new CashRegister(
                LocalDate.now(),
                relatorio,
                entry.getQuartos().getNumero(),
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
        EntryConsumption entryConsumption = new EntryConsumption(
            0,
            semConsumo,
                entry
        );
        entryConsumptionService.addConsumo(entryConsumption);
    }

    public List<Entry> findByStatusEntrada(EntryStatus entryStatus){
        return entryRepository.findEntradasByStatusEntrada(entryStatus);
    }

    public List<Entry> findEntradaByToday(){
       LocalDate today = LocalDate.now();
       return entryRepository.findEntradasByDataRegistroEntrada(today);
    }
    public List<Entry> findEntradaByDate(LocalDate data){
        return entryRepository.findEntradasByDataRegistroEntrada(data);
    }

    private void atualizaQuarto(Rooms rooms, Entry entradaAtualizada){
        rooms = entry.getQuartos();
        rooms.setRoomStatus(RoomStatus.AVAIABLE);
        quartosFeing.save(rooms);
        entradaAtualizada.setStatusEntrada(EntryStatus.FINISH);
    }
}