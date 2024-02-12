package com.example.PousadaIstoE.services;

import com.example.PousadaIstoE.Enums.EntryStatus;
import com.example.PousadaIstoE.Enums.PaymentStatus;
import com.example.PousadaIstoE.Enums.PaymentType;
import com.example.PousadaIstoE.Enums.RoomStatus;
import com.example.PousadaIstoE.builders.EntryBuilder;
import com.example.PousadaIstoE.exceptions.EntityConflict;
import com.example.PousadaIstoE.model.Entry;
import com.example.PousadaIstoE.model.Rooms;
import com.example.PousadaIstoE.repository.EntryConsumptionRepository;
import com.example.PousadaIstoE.repository.EntryRepository;
import com.example.PousadaIstoE.repository.RoomRepository;
import com.example.PousadaIstoE.request.EntryRequest;
import com.example.PousadaIstoE.request.UpdateEntryRequest;
import com.example.PousadaIstoE.response.ConsumptionResponse;
import com.example.PousadaIstoE.response.EntryResponse;
import com.example.PousadaIstoE.response.SimpleEntryResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EntryService2 {
    public static final Float ENTRY_VALUE = 30F;
    private final EntryRepository entryRepository;
    private final EntryConsumptionRepository entryConsumptionRepository;
    private final Finder find;
    private final RoomRepository roomRepository;

    public EntryService2(EntryRepository entryRepository, EntryConsumptionRepository entryConsumptionRepository, Finder find,
                         RoomRepository roomRepository) {
        this.entryRepository = entryRepository;
        this.entryConsumptionRepository = entryConsumptionRepository;
        this.find = find;
        this.roomRepository = roomRepository;
    }

    public Page<SimpleEntryResponse> findAll(Pageable pageable) {
        Page<Entry> page = entryRepository.findAllOrderByIdDesc(pageable);
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
        roomVerification(room);
        room.setRoomStatus(RoomStatus.BUSY);
        Entry newEntry = new EntryBuilder()
                .rooms(room)
                .startTime(LocalTime.now())
                .licensePlate(request.licensePlate().toUpperCase())
                .entryStatus(EntryStatus.IN_PROGRESS)
                .entryDataRegister(LocalDate.now())
                .paymentStatus(PaymentStatus.PENDING)
                .paymentType(PaymentType.PENDING)
                .totalEntry(ENTRY_VALUE)
                .build();
        roomRepository.save(room);
        entryRepository.save(newEntry);
    }

    public void updateEntry(Long entry_id, UpdateEntryRequest request){

        var entry = find.entryById(entry_id);
        if (entry.getEntryStatus().equals(EntryStatus.FINISHED)) throw new EntityConflict("The Entry was finished");

        var room = find.roomById(request.room_id());
        Entry updatedEntry = new EntryBuilder()
                .id(entry.getId())
                .rooms(room)
                .startTime(entry.getStartTime())
                .licensePlate(request.vehicle_plate().toUpperCase())
                .entryStatus(request.entry_status())
                .entryDataRegister(entry.getEntryDataRegister())
                .paymentStatus(request.payment_status())
                .paymentType(request.payment_type())
                .totalEntry(entry.getTotalEntry())
                .build();

        if (updatedEntry.getEntryStatus().equals(EntryStatus.FINISH)){
            updatedEntry.setEndTime(LocalTime.now());
            calculateHours(entry_id);
        }



        entryRepository.save(updatedEntry);
    }





    private EntryResponse entryResponse(Entry entry){
        Double totalConsumption = entryRepository.totalConsumptionByEntryId(entry.getId());
        totalConsumption = totalConsumption != null ? totalConsumption : 0.0;

        var consumptionList = entryConsumptionRepository.findEntryConsumptionByEntry_Id(entry.getId());
        List<ConsumptionResponse> consumptionResponseList = new ArrayList<>();
        consumptionList.forEach(entryConsumption -> {
            ConsumptionResponse consumptionResponse = new ConsumptionResponse(
                    entryConsumption.getAmount(),
                    entryConsumption.getItens().getDescription(),
                    entryConsumption.getItens().getValue(),
                    entryConsumption.getTotal()
                    );
            consumptionResponseList.add(consumptionResponse);
        });
        var total = entry.getTotalEntry() + totalConsumption;
        return new EntryResponse(
                entry.getId(),
                entry.getRooms().getNumber(),
                entry.getEntryDataRegister(),
                entry.getStartTime(),
                entry.getEndTime() != null ? entry.getEndTime() : LocalTime.of(0,0),
                entry.getLicensePlate() != null ? entry.getLicensePlate() : "",
                timeSpent(entry),
                consumptionResponseList,
                entry.getEntryStatus(),
                totalConsumption,
                entry.getTotalEntry(),
                total
                );
    }

    private SimpleEntryResponse simpleEntryResponse(Entry entry) {
        return new SimpleEntryResponse(
                entry.getId(),
                entry.getRooms().getNumber(),
                entry.getStartTime(),
                entry.getEndTime(),
                entry.getLicensePlate() != null ? entry.getLicensePlate() : "",
                entry.getEntryStatus()
        );
    }

    private String timeSpent(Entry entry){
        Duration timeSpent = Duration.between(
                entry.getStartTime(),
                entry.getEndTime() != null ? entry.getEndTime() : LocalTime.now());
        var hour = timeSpent.toHours();
        var minutes = timeSpent.toMinutes();
        String response = "";

        if (timeSpent.toMinutes() < 60) response = minutes + " Min";
        else response = hour + " Hrs";
        return response;
    }

    private void calculateHours(Long entry_id){
        var entry = find.entryById(entry_id);
        Duration timeSpent = Duration.between(
                entry.getStartTime(),
                entry.getEndTime() != null ? entry.getEndTime() : LocalTime.now());
        var minutes = timeSpent.toMinutes();
        int hours = (int) (minutes / 60);
        if (hours > 2){
             minutes = (int) ((((float) minutes - 120) / 30) * 5);
             entry.setTotalEntry(entry.getTotalEntry()+ minutes);
        }
    }


    private void roomVerification(Rooms rooms){
        switch (rooms.getRoomStatus()) {
            case BUSY -> throw new EntityConflict("The room is busy");
            case NEEDS_CLEANING -> throw new EntityConflict("The room needs cleaning!");
            case RESERVED -> throw new EntityConflict("Room Reserved!");
        }
    }

}
