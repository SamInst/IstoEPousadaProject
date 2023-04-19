package com.example.PousadaIstoE.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "tb_pernoite_consumo")
public class ConsumoPernoite {
    private Integer quantidade;

    public Integer getQuantidade() {
        return quantidade;
    }
    @ManyToOne
    private Pernoites pernoites;

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

    public Pernoites getPernoites() {
        return pernoites;
    }
}
