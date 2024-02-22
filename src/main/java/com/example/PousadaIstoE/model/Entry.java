package com.example.PousadaIstoE.model;

import com.example.PousadaIstoE.Enums.EntryStatus;
import com.example.PousadaIstoE.Enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
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

    @OneToMany
    @JoinColumn (name = "ip03_payment_type")
    private List<PaymentType> paymentType;

    @Enumerated(EnumType.STRING)
    @Column(name = "ip03_payment_status")
    private PaymentStatus paymentStatus;

    @Column(name = "ip03_obs")
    private String obs;

    @Column(name = "ip03_entry_value")
    private Float entryValue;
    @Column(name = "ip03_consumption_value")
    private Float consumptionValue;

    public Entry() {
    }

    public Entry(Long id, Rooms rooms, LocalDateTime startTime, LocalDateTime endTime, String licensePlate, EntryStatus entryStatus, LocalDate entryDataRegister, Float totalEntry, List<PaymentType> paymentType, PaymentStatus paymentStatus, String obs, Float entryValue, Float consumptionValue) {
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
        this.obs = obs;
        this.entryValue = entryValue;
        this.consumptionValue = consumptionValue;
    }
}
