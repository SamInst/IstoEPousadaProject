package com.example.PousadaIstoE.model;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Setter
@Getter
@NoArgsConstructor
@Table(name = "ip05_cash_register")
@Entity
public class CashRegister {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ip05_id")
    private Long id;

    @Column(name = "ip05_data")
    private LocalDate date;

    @Column(name = "ip05_report")
    private String report;

    @Column(name = "ip05_apartment")
    private Integer apartment;

    @Column(name = "ip05_cash_in")
    private Float cashIn;

    @Column(name = "ip05_cash_out")
    private Float cashOut;

    @Column(name = "ip05_total")
    private Float total;

    @Column(name = "ip05_hour")
    private LocalTime hour;
}