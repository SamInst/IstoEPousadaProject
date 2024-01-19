package com.example.PousadaIstoE.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
@Table(name = "ip10_overnight_consumption")
public class OverNightStayConsumption {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ip10_id")
    private Long id;

    @Column(name = "ip10_quantidade")
    private Integer quantidade;

    @ManyToOne
    @JoinColumn(name = "fkip10ip07_itens_id")
    private Itens itens;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "fkip10ip08_overnight_stay_id")
    private OvernightStay overnightStay;

    @Column(name = "ip10_total")
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

    public OvernightStay getPernoites() {
        return overnightStay;
    }

    public void setPernoites(OvernightStay overnightStay) {
        this.overnightStay = overnightStay;
    }

    public OverNightStayConsumption(Integer quantidade, Itens itens, OvernightStay overnightStay) {
        this.quantidade = quantidade;
        this.itens = itens;
        this.overnightStay = overnightStay;
        this.total = quantidade.floatValue() * itens.getValue();
    }
    public OverNightStayConsumption() {
    }
}