package com.example.PousadaIstoE.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@Entity
@Table(name = "a03_daily_value")
public class DailyValues {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    private Integer amountPeople;
    private Float price;

    public DailyValues(Integer amountPeople, Float price) {
        this.amountPeople = amountPeople;
        this.price = price;
    }
}
