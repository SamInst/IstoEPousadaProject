package com.example.PousadaIstoE.model;

import jakarta.persistence.*;

@Entity
public class EntradaConsumo {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private Integer quantidade;

    @ManyToOne
    private Itens itens;

    @ManyToOne
    private Entradas entradas;

    private Float total;


    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
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

    public EntradaConsumo(Integer quantidade, Itens itens, Entradas entradas) {
        this.quantidade = quantidade;
        this.itens = itens;
        this.entradas = entradas;
        this.total = quantidade.floatValue() * itens.getValor();
    }

    public EntradaConsumo() {
    }
}
