package com.example.PousadaIstoE.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
@Table(name = "ip06_entrada_consumption")
public class EntradaConsumption {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ip06_id")
    private Long id;

    @Column(name = "ip06_quantidade")
    private Integer quantity;

    @ManyToOne
    @JoinColumn(name = "fkip06ip07_itens_id")
    private Itens itens;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "fkip06ip03_entradas_id")
    private Entradas entradas;

    @Column(name = "ip06_total")
    private Float total;


    public void setId(Long id) {
        this.id = id;
    }
    public Long getId() {
        return id;
    }
    public Integer getQuantity() {
        return quantity;
    }
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
    public Itens getItens() {
        return itens;
    }
    public void setItens(Itens itens) {
        this.itens = itens;
    }
    public Entradas getEntradas() {
        return entradas;
    }
    public void setEntradas(Entradas entradas) {
        this.entradas = entradas;
    }
    public Float getTotal() {
        return total;
    }
    public void setTotal(Float total) {
        this.total = total;
    }

    public EntradaConsumption(Integer quantity, Itens itens, Entradas entradas) {
        this.quantity = quantity;
        this.itens = itens;
        this.entradas = entradas;
        this.total = quantity.floatValue() * itens.getValue();
    }
    public EntradaConsumption() {
    }
}