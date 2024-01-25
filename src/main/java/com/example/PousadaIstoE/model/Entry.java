package com.example.PousadaIstoE.model;

import com.example.PousadaIstoE.Enums.EntryStatus;
import com.example.PousadaIstoE.Enums.PaymentStatus;
import com.example.PousadaIstoE.Enums.PaymentType;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Entity
@Table(name = "ip03_entradas")
public class Entry {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "ip03_id")
    private Long id;
    @ManyToOne(cascade = CascadeType.ALL)
    private Rooms rooms;
    private LocalTime startTime;
    private LocalTime endTime;
    private String placa;
    @OneToMany
    private List<EntryConsumption> entryConsumption;
    private EntryStatus entryStatus;
    private LocalDate dataRegistroEntrada;
    private Float total_entrada;
    private PaymentType paymentType;
    private PaymentStatus status_pagamento;

    public Long getId() {return id;}
    public void setId(Long id) {this.id = id;}
    public Rooms getRooms() {return rooms;}
    public void setRooms(Rooms rooms) {this.rooms = rooms;}
    public LocalTime getStartTime() {return startTime;}
    public void setStartTime(LocalTime startTime) {this.startTime = startTime;}
    public LocalTime getEndTime() {return endTime;}
    public void setEndTime(LocalTime endTime) {this.endTime = endTime;}
    public String getPlaca() {return placa;}
    public void setPlaca(String placa) {this.placa = placa;}
    public List<EntryConsumption> getEntryConsumption() {return entryConsumption;}
    public void setEntryConsumption(List<EntryConsumption> entryConsumption) {this.entryConsumption = entryConsumption;}
    public EntryStatus getEntryStatus() {return entryStatus;}
    public void setEntryStatus(EntryStatus entryStatus) {this.entryStatus = entryStatus;}
    public LocalDate getDataRegistroEntrada() {return dataRegistroEntrada;}
    public void setDataRegistroEntrada(LocalDate dataRegistroEntrada) {this.dataRegistroEntrada = dataRegistroEntrada;}
    public Float getTotal_entrada() {return total_entrada;}
    public void setTotal_entrada(Float total_entrada) {this.total_entrada = total_entrada;}
    public PaymentType getPaymentType() {return paymentType;}
    public void setPaymentType(PaymentType paymentType) {this.paymentType = paymentType;}
    public PaymentStatus getStatus_pagamento() {return status_pagamento;}
    public void setStatus_pagamento(PaymentStatus status_pagamento) {this.status_pagamento = status_pagamento;}

    public Entry() {
    }

    public Entry(Long id, Rooms rooms, LocalTime startTime, LocalTime endTime, String placa) {
        this.id = id;
        this.rooms = rooms;
        this.startTime = startTime;
        this.endTime = endTime;
        this.placa = placa;
    }

    public Entry(Long id, Rooms rooms, LocalTime startTime, LocalTime endTime, String placa, PaymentType paymentType, PaymentStatus status_pagamento, EntryStatus entryStatus) {
        this.id = id;
        this.rooms = rooms;
        this.startTime = startTime;
        this.endTime = endTime;
        this.placa = placa;
        this.paymentType = paymentType;
        this.status_pagamento = status_pagamento;
        this.entryStatus = entryStatus;
    }

    public Entry(Rooms rooms, LocalTime startTime, LocalTime endTime, String placa, EntryStatus entryStatus, LocalDate dataRegistroEntrada, PaymentType paymentType, PaymentStatus status_pagamento) {
        this.rooms = rooms;
        this.startTime = startTime;
        this.endTime = endTime;
        this.placa = placa;
        this.entryStatus = entryStatus;
        this.dataRegistroEntrada = dataRegistroEntrada;
        this.paymentType = paymentType;
        this.status_pagamento = status_pagamento;
    }
}
