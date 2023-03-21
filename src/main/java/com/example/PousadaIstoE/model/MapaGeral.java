package com.example.PousadaIstoE.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDate;

@Table(name = "tb_mapa_geral")
@Entity
public class MapaGeral {
    @Id
    private Long id;

    private LocalDate data;
    private String report;
    private Integer apartment;
    private Float entrada;
    private Float saida;
    private Float total = entrada + saida;

    public Float getEntrada() {
        return entrada;
    }

    public void setEntrada(Float entrada) {
        this.entrada = entrada;
    }

    public Float getSaida() {
        return saida;
    }

    public void setSaida(Float saida) {
        this.saida = saida;
    }

    public MapaGeral(LocalDate data, String report, Integer apartment, Float entrada, Float saida) {
        this.data = data;
        this.report = report;
        this.apartment = apartment;
        this.entrada = entrada;
        this.saida = saida;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate today) {
        this.data = today;
    }

    public String getReport() {
        return report;
    }

    public void setReport(String report) {
        this.report = report;
    }

    public Integer getApartment() {
        return apartment;
    }

    public void setApartment(Integer apartment) {
        this.apartment = apartment;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public MapaGeral() {
    }
}
