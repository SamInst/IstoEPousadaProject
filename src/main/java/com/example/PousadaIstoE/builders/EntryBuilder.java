package com.example.PousadaIstoE.builders;

import com.example.PousadaIstoE.Enums.EntryStatus;
import com.example.PousadaIstoE.Enums.PaymentStatus;
import com.example.PousadaIstoE.Enums.PaymentType;
import com.example.PousadaIstoE.model.Entry;
import com.example.PousadaIstoE.model.Rooms;
import java.time.LocalDate;
import java.time.LocalTime;

public class EntryBuilder {
    private Long id;
    private Rooms rooms;
    private LocalTime startTime;
    private LocalTime endTime;
    private String licensePlate;
    private EntryStatus entryStatus;
    private LocalDate entryDataRegister;
    private Float totalEntry;
    private PaymentType paymentType;
    private PaymentStatus paymentStatus;

    public EntryBuilder id(Long id) {
        this.id = id;
        return this;
    }

    public EntryBuilder rooms(Rooms rooms) {
        this.rooms = rooms;
        return this;
    }

    public EntryBuilder startTime(LocalTime startTime) {
        this.startTime = startTime;
        return this;
    }

    public EntryBuilder endTime(LocalTime endTime) {
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

    public EntryBuilder paymentType(PaymentType paymentType) {
        this.paymentType = paymentType;
        return this;
    }

    public EntryBuilder paymentStatus(PaymentStatus paymentStatus) {
        this.paymentStatus = paymentStatus;
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
                paymentStatus);
    }
}