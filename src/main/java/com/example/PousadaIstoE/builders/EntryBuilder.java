package com.example.PousadaIstoE.builders;

import com.example.PousadaIstoE.Enums.EntryStatus;
import com.example.PousadaIstoE.Enums.PaymentStatus;
import com.example.PousadaIstoE.model.Entry;
import com.example.PousadaIstoE.model.PaymentType;
import com.example.PousadaIstoE.model.Rooms;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class EntryBuilder {
    private Long id;
    private Rooms rooms;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String licensePlate;
    private EntryStatus entryStatus;
    private LocalDate entryDataRegister;
    private Float totalEntry;
    private List<PaymentType> paymentType;
    private PaymentStatus paymentStatus;
    private String obs;
    private Float consumptionValue;
    private Float entryValue;
    private boolean active;

    public EntryBuilder id(Long id) {
        this.id = id;
        return this;
    }
    public EntryBuilder obs(String obs) {
        this.obs = obs;
        return this;
    }

    public EntryBuilder consumptionValue(Float consumptionValue) {
        this.consumptionValue = consumptionValue;
        return this;
    }
    public EntryBuilder entryValue(Float entryValue) {
        this.entryValue = entryValue;
        return this;
    }

    public EntryBuilder rooms(Rooms rooms) {
        this.rooms = rooms;
        return this;
    }

    public EntryBuilder startTime(LocalDateTime startTime) {
        this.startTime = startTime;
        return this;
    }

    public EntryBuilder endTime(LocalDateTime endTime) {
        this.endTime = endTime;
        return this;
    }

    public EntryBuilder licensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
        return this;
    }

    public EntryBuilder entryStatus(EntryStatus entryStatus) {
        this.entryStatus = entryStatus;
        return this;
    }

    public EntryBuilder entryDataRegister(LocalDate entryDataRegister) {
        this.entryDataRegister = entryDataRegister;
        return this;
    }

    public EntryBuilder totalEntry(Float totalEntry) {
        this.totalEntry = totalEntry;
        return this;
    }

    public EntryBuilder paymentType(List<PaymentType> paymentType) {
        this.paymentType = paymentType;
        return this;
    }

    public EntryBuilder paymentStatus(PaymentStatus paymentStatus) {
        this.paymentStatus = paymentStatus;
        return this;
    }

    public EntryBuilder active(boolean active) {
        this.active = active;
        return this;
    }

    public Entry build() {
        return new Entry(
                id,
                rooms,
                startTime,
                endTime,
                licensePlate,
                entryStatus,
                entryDataRegister,
                totalEntry,
                paymentType,
                paymentStatus,
                obs,
                entryValue,
                consumptionValue,
                active);
    }
}