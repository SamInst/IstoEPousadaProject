package com.example.PousadaIstoE.model;

import jakarta.persistence.*;

@Entity
public class RegistroConsumoEntrada {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private Integer quantidade;

    @ManyToOne
    private Itens itens;

    @ManyToOne
    private RegistroDeEntradas registroDeEntradas;

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

    public RegistroDeEntradas getRegistroDeEntradas() {
        return registroDeEntradas;
    }

    public void setRegistroDeEntradas(RegistroDeEntradas registroDeEntradas) {
        this.registroDeEntradas = registroDeEntradas;
    }

    public Float getTotal() {
        return total;
    }

    public void setTotal(Float total) {
        this.total = total;
    }
}
