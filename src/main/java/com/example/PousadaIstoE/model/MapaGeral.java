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

    LocalDate today;
    private String report;
    private Integer apartment;

    public LocalDate getToday() {
        return today;
    }

    public void setToday(LocalDate today) {
        this.today = today;
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

    public MapaGeral(Long id, LocalDate today, String report, Integer apartment) {
        this.id = id;
        this.today = today;
        this.report = report;
        this.apartment = apartment;
    }

    public MapaGeral() {
    }
}
