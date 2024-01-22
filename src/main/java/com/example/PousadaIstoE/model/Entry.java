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
    private LocalTime horaEntrada;
    private LocalTime horaSaida;
    private String placa;
    @OneToMany
    private List<EntryConsumption> entryConsumption;
    private EntryStatus entryStatus;
    private LocalDate dataRegistroEntrada;
    private Float total_entrada;

    public Entry() {
    }
    public void setId(Long id) {
        this.id = id;
    }
    public Long getId() {
        return id;
    }

    public LocalTime getHoraEntrada() {
        return horaEntrada;
    }

    public void setHoraEntrada(LocalTime horaEntrada) {
        this.horaEntrada = horaEntrada;
    }

    public LocalTime getHoraSaida() {
        return horaSaida;
    }
    public void setHoraSaida(LocalTime horaSaida) {
        this.horaSaida = horaSaida;
    }
    public String getPlaca() {
        return placa;
    }
    private PaymentType paymentType;
    private PaymentStatus status_pagamento;

    public Entry(Long id, Rooms rooms, LocalTime horaEntrada, LocalTime horaSaida, String placa) {
        this.id = id;
        this.rooms = rooms;
        this.horaEntrada = horaEntrada;
        this.horaSaida = horaSaida;
        this.placa = placa;
    }

    public Entry(Long id, Rooms rooms, LocalTime horaEntrada, LocalTime horaSaida, String placa, PaymentType paymentType, PaymentStatus status_pagamento, EntryStatus entryStatus) {
        this.id = id;
        this.rooms = rooms;
        this.horaEntrada = horaEntrada;
        this.horaSaida = horaSaida;
        this.placa = placa;
        this.paymentType = paymentType;
        this.status_pagamento = status_pagamento;
        this.entryStatus = entryStatus;
    }

    public Entry(Rooms rooms, LocalTime horaEntrada, LocalTime horaSaida, String placa, EntryStatus entryStatus, LocalDate dataRegistroEntrada, PaymentType paymentType, PaymentStatus status_pagamento) {
        this.rooms = rooms;
        this.horaEntrada = horaEntrada;
        this.horaSaida = horaSaida;
        this.placa = placa;
        this.entryStatus = entryStatus;
        this.dataRegistroEntrada = dataRegistroEntrada;
        this.paymentType = paymentType;
        this.status_pagamento = status_pagamento;
    }

    public PaymentType getTipoPagamento() {
        return paymentType;
    }

    public LocalDate getDataRegistroEntrada() {
        return dataRegistroEntrada;
    }

    public void setDataRegistroEntrada(LocalDate dataRegistroEntrada) {
        this.dataRegistroEntrada = dataRegistroEntrada;
    }

    public void setTipoPagamento(PaymentType paymentType) {
        this.paymentType = paymentType;
    }
    public PaymentStatus getStatus_pagamento() {
        return status_pagamento;
    }
    public void setStatus_pagamento(PaymentStatus status_pagamento) {
        this.status_pagamento = status_pagamento;
    }
    public List<EntryConsumption> getEntradaConsumo() {
        return entryConsumption;
    }
    public Rooms getQuartos() {return rooms;}
    public void setQuartos(Rooms rooms) {
        this.rooms = rooms;
    }
    public EntryStatus getStatusEntrada() {
        return entryStatus;
    }
    public void setStatusEntrada(EntryStatus entryStatus) {
        this.entryStatus = entryStatus;
    }
    public void setPlaca(String placa) {
        this.placa = placa;
    }
    public void setEntradaConsumo(List<EntryConsumption> entryConsumption) {
        this.entryConsumption = entryConsumption;
    }
    public Float getTotal_entrada() {
        return total_entrada;
    }
    public void setTotal_entrada(Float total_entrada) {
        this.total_entrada = total_entrada;
    }
}
