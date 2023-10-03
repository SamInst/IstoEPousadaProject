package com.example.PousadaIstoE.model;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Table(name = "tb_mapa_geral")
@Entity
public class MapaGeral {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDate data;
    private String report;
    private Integer apartment;
    private Float entrada;
    private Float saida;
    private Float total;
    private LocalTime hora;

    public LocalTime getHora() {return hora;}
    public void setHora(LocalTime hora) {this.hora = hora;}
    public Float getTotal() {
        return total;
    }
    public void setTotal(Float total) {
        this.total = total;
    }
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
    public LocalDate getData() {
        return data;
    }
    public void setData(LocalDate today) {
        this.data = today;
    }
    public String getReport() {
        return report;
    }
    public Integer getApartment() {
        return apartment;
    }

    public void setReport(String report) {
        this.report = report;
    }

    public void setApartment(Integer apartment) {
        this.apartment = apartment;
    }

    public MapaGeral() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public MapaGeral(LocalDate data, String report, Integer apartment, Float entrada) {
        this.data = data;
        this.report = report;
        this.apartment = apartment;
        this.entrada = entrada;
    }
    public MapaGeral(LocalDate data, String report, Integer apartment, Float entrada, Float saida, Float total, LocalTime hora) {
        this.data = data;
        this.report = report;
        this.apartment = apartment;
        this.entrada = entrada;
        this.saida = saida;
        this.total = total;
        this.hora = hora;
    }
}