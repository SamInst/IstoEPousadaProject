package com.example.PousadaIstoE.model;

import com.example.PousadaIstoE.response.StatusPagamento;
import com.example.PousadaIstoE.response.TipoPagamento;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Table(name = "tb_registro_de_entradas")
@Entity
public class RegistroDeEntradas {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private Integer apt;
    private LocalTime horaEntrada;
    private LocalTime horaSaida;
    private String consumo;
    private String placa;
    private LocalDate data;
    private Integer horas;
    private Integer minutos;
    private  Double total;

    @OneToMany
    private List<RegistroConsumoEntrada> entradaConsumo;

    public RegistroDeEntradas() {
    }
    public void setId(Long id) {
        this.id = id;
    }
    public Long getId() {
        return id;
    }
    public Integer getApt() {
        return apt;
    }
    public void setApt(Integer apt) {
        this.apt = apt;
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
    public String getConsumo() {
        return consumo;
    }
    public void setConsumo(String consumo) {
        this.consumo = consumo;
    }
    public String getPlaca() {
        return placa;
    }
    public void setPlaca(String placa) {
        this.placa = placa;
    }
    private TipoPagamento tipoPagamento;
    private StatusPagamento status_pagamento;
    public TipoPagamento getTipoPagamento() {
        return tipoPagamento;
    }
    public void setTipoPagamento(TipoPagamento tipoPagamento) {
        this.tipoPagamento = tipoPagamento;
    }
    public StatusPagamento getStatus_pagamento() {
        return status_pagamento;
    }
    public void setStatus_pagamento(StatusPagamento status_pagamento) {
        this.status_pagamento = status_pagamento;
    }
    public LocalDate getData() {
        return data;
    }
    public void setData(LocalDate data) {
        this.data = data;
    }
    public Integer getHoras() {
        return horas;
    }
    public void setHoras(Integer horas) {
        this.horas = horas;
    }
    public Integer getMinutos() {
        return minutos;
    }
    public void setMinutos(Integer minutos) {
        this.minutos = minutos;
    }
    public Double getTotal() {
        return total;
    }
    public void setTotal(Double total) {
        this.total = total;
    }

    public RegistroDeEntradas(Long id, Integer apt, LocalTime horaEntrada, LocalTime horaSaida, String consumo, String placa, LocalDate data, Integer horas, Integer minutos, Double total, List<RegistroConsumoEntrada> entradaConsumo, TipoPagamento tipoPagamento, StatusPagamento status_pagamento) {
        this.id = id;
        this.apt = apt;
        this.horaEntrada = horaEntrada;
        this.horaSaida = horaSaida;
        this.consumo = consumo;
        this.placa = placa;
        this.data = data;
        this.horas = horas;
        this.minutos = minutos;
        this.total = total;
        this.entradaConsumo = entradaConsumo;
        this.tipoPagamento = tipoPagamento;
        this.status_pagamento = status_pagamento;
    }

    public List<RegistroConsumoEntrada> getEntradaConsumo() {
        return entradaConsumo;
    }

    public void setEntradaConsumo(List<RegistroConsumoEntrada> entradaConsumo) {
        this.entradaConsumo = entradaConsumo;
    }
}
