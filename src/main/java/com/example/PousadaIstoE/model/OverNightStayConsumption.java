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

    @Column(name = "ip10_amount")
    private Integer amount;

    @ManyToOne
    @JoinColumn(name = "fkip10ip07_itens_id")
    private Item item;

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
    public Integer getAmount() {
        return amount;
    }
    public void setAmount(Integer amount) {
        this.amount = amount;
    }
    public Item getItens() {
        return item;
    }
    public void setItens(Item item) {
        this.item = item;
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

    public OverNightStayConsumption(Integer amount, Item item, OvernightStay overnightStay) {
        this.amount = amount;
        this.item = item;
        this.overnightStay = overnightStay;
        this.total = amount.floatValue() * item.getValue();
    }
    public OverNightStayConsumption() {
    }
}