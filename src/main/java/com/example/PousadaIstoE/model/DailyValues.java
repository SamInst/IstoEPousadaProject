package com.example.PousadaIstoE.model;

import jakarta.persistence.*;

@Entity
@Table(name = "a03_daily_value")
public class DailyValues {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    private Integer amountPeople;
    private Float price;

    public DailyValues() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getAmountPeople() {
        return amountPeople;
    }

    public void setAmountPeople(Integer amountPeople) {
        this.amountPeople = amountPeople;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public DailyValues(Integer amountPeople, Float price) {
        this.amountPeople = amountPeople;
        this.price = price;
    }
}
