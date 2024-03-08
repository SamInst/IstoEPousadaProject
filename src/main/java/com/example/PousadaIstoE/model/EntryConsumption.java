package com.example.PousadaIstoE.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
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

    public EntryConsumption(Integer amount, Item item, Entry entry) {
        this.amount = amount;
        this.item = item;
        this.entry = entry;
        this.total = amount.floatValue() * item.getValue();
    }
}