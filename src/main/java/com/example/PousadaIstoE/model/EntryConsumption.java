package com.example.PousadaIstoE.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
@Table(name = "ip06_entry_consumption")
public class EntryConsumption {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ip06_id")
    private Long id;

    @Column(name = "ip06_amount")
    private Integer amount;

    @ManyToOne
    @JoinColumn(name = "fkip06ip07_itens_id")
    private Item item;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "fkip06ip03_entry_id")
    private Entry entry;

    @Column(name = "ip06_total")
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
    public Entry getEntradas() {
        return entry;
    }
    public void setEntradas(Entry entry) {
        this.entry = entry;
    }
    public Float getTotal() {
        return total;
    }
    public void setTotal(Float total) {
        this.total = total;
    }

    public EntryConsumption(Integer amount, Item item, Entry entry) {
        this.amount = amount;
        this.item = item;
        this.entry = entry;
        this.total = amount.floatValue() * item.getValue();
    }
    public EntryConsumption() {
    }
}