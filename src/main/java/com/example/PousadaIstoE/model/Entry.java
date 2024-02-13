package com.example.PousadaIstoE.model;

import com.example.PousadaIstoE.Enums.EntryStatus;
import com.example.PousadaIstoE.Enums.PaymentStatus;
import com.example.PousadaIstoE.Enums.PaymentType;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "ip03_entry")
public class Entry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ip03_id")
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "ip03_rooms_id")
    private Rooms rooms;

    @Column(name = "ip03_start_time")
    private LocalDateTime startTime;

    @Column(name = "ip03_end_time")
    private LocalDateTime endTime;

    @Column(name = "ip03_license_plate")
    private String licensePlate;

    @Enumerated(EnumType.STRING)
    @Column(name = "ip03_entry_status")
    private EntryStatus entryStatus;

    @Column(name = "ip03_entry_data_register")
    private LocalDate entryDataRegister;

    @Column(name = "ip03_total_entry")
    private Float totalEntry;

    @Enumerated(EnumType.STRING)
    @Column(name = "ip03_payment_type")
    private PaymentType paymentType;

    @Enumerated(EnumType.STRING)
    @Column(name = "ip03_payment_status")
    private PaymentStatus paymentStatus;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Rooms getRooms() {
        return rooms;
    }

    public void setRooms(Rooms rooms) {
        this.rooms = rooms;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }

    public EntryStatus getEntryStatus() {
        return entryStatus;
    }

    public void setEntryStatus(EntryStatus entryStatus) {
        this.entryStatus = entryStatus;
    }

    public LocalDate getEntryDataRegister() {
        return entryDataRegister;
    }

    public void setEntryDataRegister(LocalDate entryDataRegister) {
        this.entryDataRegister = entryDataRegister;
    }

    public Float getTotalEntry() {
        return totalEntry;
    }

    public void setTotalEntry(Float totalEntry) {
        this.totalEntry = totalEntry;
    }

    public PaymentType getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(PaymentType paymentType) {
        this.paymentType = paymentType;
    }

    public PaymentStatus getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(PaymentStatus paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public Entry() {
    }

    public Entry(Long id, Rooms rooms, LocalDateTime startTime, LocalDateTime endTime, String licensePlate) {
        this.id = id;
        this.rooms = rooms;
        this.startTime = startTime;
        this.endTime = endTime;
        this.licensePlate = licensePlate;
    }

    public Entry(Long id, Rooms rooms, LocalDateTime startTime, LocalDateTime endTime, String licensePlate, PaymentType paymentType, PaymentStatus status_pagamento, EntryStatus entryStatus) {
        this.id = id;
        this.rooms = rooms;
        this.startTime = startTime;
        this.endTime = endTime;
        this.licensePlate = licensePlate;
        this.paymentType = paymentType;
        this.paymentStatus = status_pagamento;
        this.entryStatus = entryStatus;
    }

    public Entry(Rooms rooms, LocalDateTime startTime, LocalDateTime endTime, String licensePlate, EntryStatus entryStatus, LocalDate dataRegistroEntrada, PaymentType paymentType, PaymentStatus status_pagamento) {
        this.rooms = rooms;
        this.startTime = startTime;
        this.endTime = endTime;
        this.licensePlate = licensePlate;
        this.entryStatus = entryStatus;
        this.entryDataRegister = dataRegistroEntrada;
        this.paymentType = paymentType;
        this.paymentStatus = status_pagamento;
    }

    public Entry(Long id, Rooms rooms, LocalDateTime startTime, LocalDateTime endTime, String licensePlate, EntryStatus entryStatus, LocalDate entryDataRegister, Float totalEntry, PaymentType paymentType, PaymentStatus paymentStatus) {
        this.id = id;
        this.rooms = rooms;
        this.startTime = startTime;
        this.endTime = endTime;
        this.licensePlate = licensePlate;
        this.entryStatus = entryStatus;
        this.entryDataRegister = entryDataRegister;
        this.totalEntry = totalEntry;
        this.paymentType = paymentType;
        this.paymentStatus = paymentStatus;
    }
}
