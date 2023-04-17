package com.example.PousadaIstoE.model;

import com.example.PousadaIstoE.response.StatusPagamento;
import com.example.PousadaIstoE.response.TipoPagamento;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
public class Pernoites {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private Integer apt;
    private LocalDate dataEntrada;
    private LocalDate dataSaida;
    private String Consumo;
    @OneToMany
    List<Consumo> consumoList;
    private Integer quantidadePessoa;
    @ManyToOne
    private Client client;
    private TipoPagamento tipoPagamento;
    private StatusPagamento status_pagamento;

    public Long getId() {
        return id;
    }
    public Integer getApt() {
        return apt;
    }
    public void setId(Long id) {this.id = id;}
    public Integer getQuantidadePessoa() {return quantidadePessoa;}
    public void setQuantidadePessoa(Integer quantidadePessoa) {this.quantidadePessoa = quantidadePessoa;}
    public void setApt(Integer apt) {
        this.apt = apt;
    }
    public String getConsumo() {
        return Consumo;
    }
    public void setConsumo(String consumo) {
        Consumo = consumo;
    }
    public LocalDate getDataEntrada() {
        return dataEntrada;
    }
    public LocalDate getDataSaida() {
        return dataSaida;
    }
    public Client getClient() {
        return client;
    }
    public void setDataEntrada(LocalDate dataEntrada) {
        this.dataEntrada = dataEntrada;
    }
    public void setDataSaida(LocalDate dataSaida) {
        this.dataSaida = dataSaida;
    }
    public void setClient(Client client) {
        this.client = client;
    }

    public List<com.example.PousadaIstoE.model.Consumo> getConsumoList() {
        return consumoList;
    }

    public TipoPagamento getTipoPagamento() {
        return tipoPagamento;
    }

    public StatusPagamento getStatus_pagamento() {
        return status_pagamento;
    }
}