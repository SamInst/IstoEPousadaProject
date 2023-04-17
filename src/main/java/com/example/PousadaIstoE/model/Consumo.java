package com.example.PousadaIstoE.model;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Consumo {
    private Float agua = 3F;
    private Float salgado = 4F;
    private Float bombom = 2F;
    private Float Refrigerante = 5F;
    @Id
    private Long id;


    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public Float getAgua() {
        return agua;
    }

    public Float getSalgado() {
        return salgado;
    }

    public Float getBombom() {
        return bombom;
    }

    public Float getRefrigerante() {
        return Refrigerante;
    }
}
