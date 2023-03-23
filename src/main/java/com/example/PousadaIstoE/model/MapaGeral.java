package com.example.PousadaIstoE.model;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Table(name = "tb_mapa_geral")
@Entity
public class MapaGeral {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
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
    public MapaGeral() {
    }
}