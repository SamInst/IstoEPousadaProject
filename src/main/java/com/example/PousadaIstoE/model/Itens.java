package com.example.PousadaIstoE.model;

import jakarta.persistence.*;


@Entity
@Table(name = "tb_itens")
public class Itens {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String descricao;
    private Float valor;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Float getValor() {
        return valor;
    }

    public void setValor(Float valor) {
        this.valor = valor;
    }

    public Itens(String descricao, Float valor) {
        this.descricao = descricao;
        this.valor = valor;
    }

    public Itens(Long id, String descricao, Float valor) {
        this.id = id;
        this.descricao = descricao;
        this.valor = valor;
    }

    public Itens() {
    }
}
