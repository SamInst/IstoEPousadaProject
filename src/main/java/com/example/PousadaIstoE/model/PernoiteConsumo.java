package com.example.PousadaIstoE.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
@Table(name = "tb_pernoite-consumo")
public class PernoiteConsumo {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private Integer quantidade;
    @ManyToOne
    private Itens itens;
    @JsonIgnore
    @ManyToOne
    private Pernoites pernoites;
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
    public Float getTotal() {
        return total;
    }
    public void setTotal(Float total) {
        this.total = total;
    }

    public Pernoites getPernoites() {
        return pernoites;
    }

    public void setPernoites(Pernoites pernoites) {
        this.pernoites = pernoites;
    }

    public PernoiteConsumo(Integer quantidade, Itens itens, Pernoites pernoites) {
        this.quantidade = quantidade;
        this.itens = itens;
        this.pernoites = pernoites;
        this.total = quantidade.floatValue() * itens.getValor();
    }
    public PernoiteConsumo() {
    }
}