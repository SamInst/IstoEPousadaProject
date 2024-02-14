//package com.example.PousadaIstoE.services;
//
//import com.example.PousadaIstoE.Enums.EntryStatus;
//import com.example.PousadaIstoE.Enums.PaymentStatus;
//import com.example.PousadaIstoE.Enums.RoomStatus;
//import com.example.PousadaIstoE.builders.CashRegisterBuilder;
//import com.example.PousadaIstoE.exceptions.EntityConflict;
//import com.example.PousadaIstoE.exceptions.EntityNotFound;
//import com.example.PousadaIstoE.model.EntryConsumption;
//import com.example.PousadaIstoE.model.Entry;
//import com.example.PousadaIstoE.model.CashRegister;
//import com.example.PousadaIstoE.model.Rooms;
//import com.example.PousadaIstoE.repository.*;
//
//import com.example.PousadaIstoE.response.*;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageImpl;
//import org.springframework.data.domain.Pageable;
//import org.springframework.stereotype.Service;
//
//import java.time.Duration;
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//import java.time.LocalTime;
//import java.util.ArrayList;
//import java.util.List;
//
//import static com.example.PousadaIstoE.Enums.PaymentType.*;
//
//@Service
//public class EntryService {
//    private final CashRegisterRepository cashRegisterFeing;
//    private final RoomRepository roomFeing;
//    private final ItemRepository itemFeing;
//    private final EntryConsumptionService entryConsumptionService;
//    double totalHourEntry;
//    double entryValue;
//    Duration difference;
//    int hours;
//    int minutesRemaining;
//    String report;
//    double totalValue;
//    Entry entry;
//    double entryAndConsumption;
//    Float totalCashRegister;
//    List<EntryConsumption> entryConsumptionList = new ArrayList<>();
//    private final EntryRepository entryRepository;
//    private final EntryConsumptionRepository entryConsumptionRepository;
//
//    public EntryService(CashRegisterRepository cashRegisterFeing, RoomRepository roomFeing, ItemRepository itemFeing, EntryConsumptionService entryConsumptionService, EntryRepository entryRepository, EntryConsumptionRepository entryConsumptionRepository) {
//        this.cashRegisterFeing = cashRegisterFeing;
//        this.roomFeing = roomFeing;
//        this.itemFeing = itemFeing;
//        this.entryConsumptionService = entryConsumptionService;
//        this.entryRepository = entryRepository;
//        this.entryConsumptionRepository = entryConsumptionRepository;
//    }
//
//    public Page<SimpleEntryResponse> findAll(Pageable pageable) {
//        Page<Entry> page = entryRepository.findAll(pageable);
//
//        List<SimpleEntryResponse> simpleEntryResponseList = new ArrayList<>();
//        page.forEach(entradas -> {
//            SimpleEntryResponse simpleEntryResponse = new SimpleEntryResponse(
//                    entradas.getId(),
//                    entradas.getRooms().getNumber(),
//                    entradas.getStartTime(),
//                    entradas.getEndTime(),
//                    entradas.getLicensePlate(),
//                    entradas.getEntryStatus()
//            );
//            simpleEntryResponseList.add(simpleEntryResponse);
//        });
//        return new PageImpl<>(simpleEntryResponseList, pageable, page.getTotalElements());
//    }
//
////    public AtomicReference<EntryResponse> findById(Long id) {
////        AtomicReference<EntryResponse> response = new AtomicReference<>();
////        entryConsumptionList = entryConsumptionRepository.findEntryConsumptionByEntry_Id(id);
////        final var entrada = entryRepository.findById(id).orElseThrow(
////                () -> new EntityNotFound("Entrada não foi Cadastrada ou não existe mais"));
////        calcularHora(id);
////        Double totalConsumo = entryRepository.totalConsumptionByEntryId(id);
////
////        if (totalConsumo == null){ totalConsumo = (double) 0; }
////        double soma = totalConsumo + entryValue;
////
////        List<ConsumptionResponse> consumptionResponseList = new ArrayList<>();
////        entryConsumptionList.forEach(consumo -> {
////            ConsumptionResponse consumptionResponse = new ConsumptionResponse(
////                    consumo.getAmount(),
////                    consumo.getItens().getDescription(),
////                    consumo.getItens().getValue(),
////                    consumo.getTotal()
////            );
////            consumptionResponseList.add(consumptionResponse);
////        });
////        response.set(new EntryResponse(
////                entrada.getRooms().getNumber(),
////                entrada.getStartTime(),
////                entrada.getEndTime(),
////                entrada.getLicensePlate(),
////                new EntryResponse.(
////                        hours,
////                        minutesRemaining
////                ),
////                consumptionResponseList,
////                entrada.getEntryStatus(),
////                totalConsumo,
////                entryValue,
////                soma
////        ));
////        return response;
////    }
//
////    public Entry registerEntrada(Entry entry) {
////        Rooms quartoOut = roomFeing.findById(entry.getRooms().getId())
////                .orElseThrow(()-> new EntityNotFound("Quarto não encontrado"));
////        switch (quartoOut.getRoomStatus()) {
////            case BUSY -> throw new EntityConflict("Quarto Ocupado");
////            case NEEDS_CLEANING -> throw new EntityConflict("Quarto Precisa de limpeza!");
////            case RESERVED -> throw new EntityConflict("Quarto Reservado!");
////        }
////        Entry request = new Entry(
////            quartoOut,
////            LocalDateTime.now(),
////            LocalTime.of(0,0),
////            entry.getLicensePlate(),
////            EntryStatus.IN_PROGRESS,
////            LocalDate.now(),
////            PaymentType.PENDING,
////            PaymentStatus.PENDING
////        );
////        quartoOut.setRoomStatus(RoomStatus.BUSY);
////        roomFeing.save(quartoOut);
////        return entryRepository.save(request);
////    }
//
//    public void updateEntradaData(Long entradaId, Entry request) {
//        entry = entryRepository.findById(entradaId).orElseThrow(
//                () -> new EntityNotFound("Entrada não encontrada")
//        );
//        var entradaAtualizada = new Entry(
//                entry.getId(),
//                entry.getRooms(),
//                entry.getStartTime(),
//                LocalDateTime.now(),
//                entry.getLicensePlate(),
//                request.getPaymentType(),
//                request.getPaymentStatus(),
//                entry.getEntryStatus()
//        );
//        entradaAtualizada.setEntryDataRegister(LocalDate.now());
//        entryRepository.save(entradaAtualizada
//        );
//        calcularHora(entradaAtualizada.getId());
//        validacaoPagamento(entry);
//        if (request.getPaymentType().equals(CASH)){
//                entradaAtualizada.setTotalEntry((float) entryAndConsumption);
//            entryRepository.save(entradaAtualizada);
//        }
//        if (request.getPaymentStatus().equals(PaymentStatus.COMPLETED)) {
//            if (entradaAtualizada.getEntryStatus().equals(EntryStatus.FINISH)){
//                throw new EntityConflict("A Entrada já foi salva no mapa");
//            }
////            entryConsumptionList = entryConsumptionRepository.findEntradaConsumoByEntradas_Id(entradaId);
////            entradaAtualizada.setEn(entryConsumptionList);
////            if (entradaAtualizada.getEntryConsumption().isEmpty()) { emptyConsumption(); }
//
//            validacaoHorario();
//            salvaNoMapa(request);
//            atualizaQuarto(entry.getRooms(), entradaAtualizada);
//            entryRepository.save(entradaAtualizada);
//        }
//    }
//
//    private void calcularHora(Long request) {
//        Entry entrada = entryRepository.findById(request)
//            .orElseThrow(() -> new EntityNotFound("Entity Not found")
//            );
//        difference = Duration.between(entrada.getStartTime(), entrada.getEndTime());
//        long minutos = difference.toMinutes();
//        hours = (int) (minutos / 60);
//        minutesRemaining = (int) (minutos % 60);
//
//        if (hours < 2) { totalHourEntry = 30.0; }
//        else { int minutes = (int) ((((float)minutos - 120)/30)*5);
//               totalHourEntry = 30 + minutes; }
//        entryValue = totalHourEntry;
//    }
//
//    private void validacaoPagamento(Entry request){
//        totalCashRegister = cashRegisterFeing.findLastTotal();
//        Double totalConsumo = entryRepository.totalConsumptionByEntryId(request.getId());
//        if (totalConsumo == null){ totalConsumo = 0D; }
//        entryAndConsumption = entryValue + totalConsumo;
//        totalValue = totalCashRegister + entryAndConsumption;
//    }
//
//    private void validacaoHorario(){
//        LocalTime noite = LocalTime.of(18,0,0);
//        LocalTime dia = LocalTime.of(6,0,0);
//        report = LocalTime.now().isAfter(noite) || LocalTime.now().isBefore(dia)
//                ? "ENTRADA NOITE" : "ENTRADA DIA";
//    }
//
//    private void salvaNoMapa(Entry request) {
//
//        CashRegister cashRegister = new CashRegisterBuilder()
//                .date(LocalDate.now())
//                .report(report)
//                .apartment(request.getRooms().getNumber())
//                .cashIn(request.getTotalEntry())
//                .cashOut(0F)
//                .hour(LocalTime.now())
//                .build();
//
//        switch (request.getPaymentType()){
//            case PIX -> cashRegister.setReport(report + PIX);
//
//            case CREDIT_CARD -> cashRegister.setReport(report + CREDIT_CARD);
//
//            case CASH -> cashRegister.setReport(report + CASH);
//        }
//        cashRegisterFeing.save(cashRegister);
//    }
//
//    public List<Entry> findByStatusEntrada(EntryStatus entryStatus){
//        return entryRepository.findEntriesByEntryStatus(entryStatus);
//    }
//
//    public List<Entry> findEntradaByToday(){
//       LocalDate today = LocalDate.now();
//       return entryRepository.findEntriesByEntryDataRegister(today);
//    }
//    public List<Entry> findEntradaByDate(LocalDate data){
//        return entryRepository.findEntriesByEntryDataRegister(data);
//    }
//
//    private void atualizaQuarto(Rooms rooms, Entry entradaAtualizada){
//        rooms = entry.getRooms();
//        rooms.setRoomStatus(RoomStatus.AVAIABLE);
//        roomFeing.save(rooms);
//        entradaAtualizada.setEntryStatus(EntryStatus.FINISH);
//    }
//}