package com.example.PousadaIstoE.services;

import com.example.PousadaIstoE.Enums.EntryStatus;
import com.example.PousadaIstoE.Enums.RoomStatus;
import com.example.PousadaIstoE.builders.EntryBuilder;
import com.example.PousadaIstoE.exceptions.EntityConflict;
import com.example.PousadaIstoE.model.*;
import com.example.PousadaIstoE.repository.*;
import com.example.PousadaIstoE.request.CashRegisterRequest;
import com.example.PousadaIstoE.request.EntryRequest;
import com.example.PousadaIstoE.request.UpdateEntryRequest;
import com.example.PousadaIstoE.response.PaymentResponse;
import com.example.PousadaIstoE.response.ConsumptionResponse;
import com.example.PousadaIstoE.response.EntryResponse;
import com.example.PousadaIstoE.response.SimpleEntryResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
public class EntryService {
    public static final Float ENTRY_VALUE = 30F;
    public static final Float HOUR_VALUE = 5F;
    public static final long MAX_HOUR_MINUTES = 120;
    private static final long PENDING = 1L;
    private static final long CASH = 2L;
    private final EntryRepository entryRepository;
    private final EntryConsumptionRepository entryConsumptionRepository;
    private final Finder find;
    private final RoomRepository roomRepository;
    private final CashRegisterService cashRegisterService;
    private final RoomService roomService;
    private final PaymentService paymentService;


    public EntryService(EntryRepository entryRepository,
                        EntryConsumptionRepository entryConsumptionRepository,
                        Finder find,
                        RoomRepository roomRepository,
                        CashRegisterService cashRegisterService,
                        RoomService roomService, PaymentService paymentService
    ){
        this.entryRepository = entryRepository;
        this.entryConsumptionRepository = entryConsumptionRepository;
        this.find = find;
        this.roomRepository = roomRepository;
        this.cashRegisterService = cashRegisterService;
        this.roomService = roomService;
        this.paymentService = paymentService;
    }

    public Page<SimpleEntryResponse> findAll(Pageable pageable) {
        Page<Entry> page = entryRepository.findAllOrderByIdDesc(pageable);
        List<SimpleEntryResponse> simpleEntryResponseList = page.getContent().stream()
                .map(this::simpleEntryResponse)
                .collect(Collectors.toList());
        return new PageImpl<>(simpleEntryResponseList, pageable, page.getTotalElements());
    }

    //simplify after using a boolen in scope
    public Page<SimpleEntryResponse> findAllActives(Pageable pageable) {
        Page<Entry> page = entryRepository.findAllOrderByIdDescActive(pageable);
        List<SimpleEntryResponse> simpleEntryResponseList = page.getContent().stream()
                .map(this::simpleEntryResponse)
                .collect(Collectors.toList());
        return new PageImpl<>(simpleEntryResponseList, pageable, page.getTotalElements());
    }

    public EntryResponse findById(Long entry_id){
        var entry = find.entryById(entry_id);
        return entryResponse(entry);
    }

    public void createEntry(EntryRequest request){
        var room = find.roomById(request.room_id());
        roomService.roomVerification(room);
        roomService.updateRoomStatus(room.getId(), RoomStatus.BUSY);
        Entry newEntry = new EntryBuilder()
                .rooms(room)
                .startTime(LocalDateTime.now())
                .licensePlate(request.vehicle_plate().toUpperCase())
                .entryStatus(EntryStatus.IN_PROGRESS)
                .entryDataRegister(LocalDate.now())
                .obs(request.obs().toUpperCase())
                .consumptionValue(0F)
                .active(true)
                .entryValue(ENTRY_VALUE)
                .totalEntry(ENTRY_VALUE)
                .build();

         roomRepository.save(room);
         entryRepository.save(newEntry);
    }

    public void updateEntry(Long entry_id, UpdateEntryRequest request){

        var entry = find.entryById(entry_id);
        if (entry.getEntryStatus().equals(EntryStatus.FINISHED)) throw new EntityConflict("The Entry was finished");

        var room = find.roomById(request.room_id());
        if (!entry.getRooms().getId().equals(request.room_id())){
            roomService.roomVerification(room);
            roomService.updateRoomStatus(entry.getRooms().getId(), RoomStatus.AVAILABLE);
        }
        roomService.updateRoomStatus(room.getId(), RoomStatus.BUSY);

        Entry updatedEntry = new EntryBuilder()
                .id(entry.getId())
                .rooms(room)
                .startTime(entry.getStartTime())
                .licensePlate(request.vehicle_plate().toUpperCase())
                .entryStatus(request.entry_status())
                .entryDataRegister(entry.getEntryDataRegister())
                .active(entry.getActive())
                .obs(request.obs().toUpperCase())
                .entryValue(entry.getEntryValue())
                .consumptionValue(entry.getConsumptionValue())
                .totalEntry(entry.getTotalEntry())
                .build();

        if (updatedEntry.getEntryStatus().equals(EntryStatus.FINISH)) {
            finishEntry(updatedEntry);
        }

        if (updatedEntry.getEntryStatus().equals(EntryStatus.FINISHED)){
            finishEntry(updatedEntry);

            var paymentValues = paymentService.sumAllValuesEntry(entry_id);
            var totalEntry = calculateHours(updatedEntry);
            var totalConsumption = sumConsumption(entry);
            var total = totalEntry + totalConsumption;

            if (paymentValues != total){
                throw new EntityConflict("The values entered do not correspond to the total cost of the overnight stay ");
            }
            updatedEntry.setEntryValue(totalEntry);
            updatedEntry.setConsumptionValue(totalConsumption);
            updatedEntry.setTotalEntry(total);
            saveInCashRegister(updatedEntry);
            roomService.updateRoomStatus(room.getId(), RoomStatus.AVAILABLE);
            updatedEntry.setEntryStatus(EntryStatus.FINISHED);
            updatedEntry.setActive(false);
        }
        entryRepository.save(updatedEntry);
    }

    private EntryResponse entryResponse(Entry entry){
        Double totalConsumption = entryRepository.totalConsumptionByEntryId(entry.getId());
        totalConsumption = totalConsumption != null ? totalConsumption : 0.0;

        var consumptionList = entryConsumptionRepository.findEntryConsumptionByEntry_Id(entry.getId());
        var paymentType = paymentService.findAllByEntryId(entry.getId());

        List<PaymentResponse> paymentTypeList = paymentType.stream()
                .map(PaymentService::paymentEntryResponse)
                .toList();
        List<ConsumptionResponse> consumptionResponseList = new ArrayList<>();
        consumptionList.forEach(entryConsumption -> {
            ConsumptionResponse consumptionResponse = new ConsumptionResponse(
                    entryConsumption.getId(),
                    entryConsumption.getAmount(),
                    entryConsumption.getItem().getDescription(),
                    entryConsumption.getItem().getValue(),
                    entryConsumption.getTotal()
                    );
            consumptionResponseList.add(consumptionResponse);
        });
        float consumptionValue = totalConsumption.floatValue();
        float entryValue = calculateHours(entry);
        float totalValue = consumptionValue + entryValue;
        if (entry.getEntryStatus().equals(EntryStatus.FINISHED)){
            consumptionValue = entry.getConsumptionValue();
            entryValue = entry.getEntryValue();
            totalValue = entry.getEntryValue() + entry.getConsumptionValue();
        }
        return new EntryResponse(
                entry.getId(),
                entry.getRooms().getNumber(),
                entry.getEntryDataRegister(),
                entry.getStartTime(),
                entry.getEndTime() != null ? entry.getEndTime() :
                        LocalDateTime.of(
                                LocalDate.now(),
                                LocalTime.of(0,0)),
                entry.getVehiclePlate() != null ? entry.getVehiclePlate() : "",
                timeSpent(entry),
                consumptionResponseList,
                entry.getEntryStatus(),
                paymentTypeList,
                consumptionValue,
                entryValue,
                totalValue
                );
    }

    private SimpleEntryResponse simpleEntryResponse(Entry entry) {
        return new SimpleEntryResponse(
                entry.getId(),
                entry.getRooms().getNumber(),
                entry.getStartTime(),
                entry.getEndTime(),
                entry.getVehiclePlate() != null ? entry.getVehiclePlate() : "",
                entry.getEntryStatus()
        );
    }

    private String timeSpent(Entry entry){
        Duration timeSpent = Duration.between(
                entry.getStartTime(),
                entry.getEndTime() != null ? entry.getEndTime() : LocalDateTime.now());

        var hour = timeSpent.toHours();
        var minutes = timeSpent.minusHours(hour).toMinutes();

        return String.format("%d:%02d", hour, minutes);
    }

    private float calculateHours(Entry entry){
        var newEntry = find.entryById(entry.getId());
        Duration timeSpent = Duration.between(
                newEntry.getStartTime(),
                newEntry.getEndTime() != null ? entry.getEndTime() : LocalDateTime.now());
        var minutes = timeSpent.toMinutes();

        long additionalPeriods = 0;
        if (minutes > MAX_HOUR_MINUTES){
            long remainingMinutes = minutes - MAX_HOUR_MINUTES;
            additionalPeriods = (remainingMinutes + 29) / 30 - 1;
        }
       return newEntry.getEntryValue() + (additionalPeriods * HOUR_VALUE);
    }

    private String reportValidation(String report){
        LocalTime night = LocalTime.of(18,0,0);
        LocalTime day = LocalTime.of(6,0,0);
        return report = LocalTime.now().isAfter(night) || LocalTime.now().isBefore(day)
                ? "ENTRADA NOITE " : "ENTRADA DIA ";
    }

    private void saveInCashRegister(Entry entry){
        var cash = find.paymentById(CASH);
        String newReport = "";
        AtomicReference<String> report = new AtomicReference<>(reportValidation(newReport));
        AtomicReference<Float> cashIn = new AtomicReference<>(0F);

        var paymentTypeList = paymentService.findAllByEntryId(entry.getId());

        paymentTypeList.forEach(paymentType -> {
            if (paymentTypeList.stream().anyMatch(payment -> payment.getPaymentType().equals(cash)) && paymentTypeList.size() == 1){
                report.updateAndGet(v -> v + "(" + paymentType.getPaymentType().getDescription() + ")");
                cashIn.set(entry.getTotalEntry());
            } else {
                report.updateAndGet(v -> v + "(" + paymentType.getPaymentType().getDescription()
                        + " 'R$ "+ paymentType.getValue()+ "') ");

                paymentTypeList.stream()
                        .filter(payment -> payment.getPaymentType().equals(cash))
                        .findFirst()
                        .ifPresent(payment -> cashIn.set(payment.getValue()));
            }
        });
        entry.setObs(entry.getObs() + "\n \n" + report);
        CashRegisterRequest cashRegisterRequest = new CashRegisterRequest(
                report.get(),
                entry.getRooms().getNumber(),
                cashIn.get(),
                0F);
        cashRegisterService.createCashRegister(cashRegisterRequest);
    }

    private Float sumConsumption(Entry entry){
        Double totalConsumption = entryRepository.totalConsumptionByEntryId(entry.getId());
        totalConsumption = totalConsumption != null ? totalConsumption : 0.0;
        entry.setConsumptionValue(totalConsumption.floatValue());
        return totalConsumption.floatValue();
    }

    private void finishEntry(Entry entry){
        entry.setEndTime(LocalDateTime.now());
        entryRepository.save(entry);
    }

    public void deleteEntry(Long entry_id){
        var entry = find.entryById(entry_id);
        if (!entry.getEntryStatus().equals(EntryStatus.IN_PROGRESS)){
            throw new EntityConflict("The entry can not be canceled.");
        }
        Entry updatedEntry = new EntryBuilder()
                .id(entry.getId())
                .rooms(null)
                .startTime(null)
                .licensePlate(null)
                .entryStatus(entry.getEntryStatus())
                .entryDataRegister(null)
                .obs(null)
                .entryValue(0F)
                .consumptionValue(0F)
                .totalEntry(0F)
                .build();
        roomService.updateRoomStatus(entry.getRooms().getId(), RoomStatus.AVAILABLE);
        entryRepository.save(updatedEntry);
        entryRepository.deleteById(entry_id);
    }

    public List<SimpleEntryResponse> findAllByDate(LocalDate date){
        var allEntries = entryRepository.findAllByEntryDataRegister(date);
        return allEntries.stream()
                .map(this::simpleEntryResponse)
                .toList();
    }
}
