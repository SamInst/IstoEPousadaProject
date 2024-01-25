package com.example.PousadaIstoE.builders;

import com.example.PousadaIstoE.model.CashRegister;

import java.time.LocalDate;
import java.time.LocalTime;

public class CashRegisterBuilder {
    private Long id;
    private LocalDate date;
    private String report;
    private Integer apartment;
    private Float cashIn;
    private Float cashOut;
    private Float total;
    private LocalTime hour;

    public CashRegisterBuilder id(Long id) {
        this.id = id;
        return this;
    }

    public CashRegisterBuilder date(LocalDate data) {
        this.date = data;
        return this;
    }

    public CashRegisterBuilder report(String report) {
        this.report = report;
        return this;
    }

    public CashRegisterBuilder apartment(Integer apartment) {
        this.apartment = apartment;
        return this;
    }

    public CashRegisterBuilder cashIn(Float cashIn) {
        this.cashIn = cashIn;
        return this;
    }

    public CashRegisterBuilder cashOut(Float cashOut) {
        this.cashOut = cashOut;
        return this;
    }

    public CashRegisterBuilder total(Float total) {
        this.total = total;
        return this;
    }

    public CashRegisterBuilder hour(LocalTime hour) {
        this.hour = hour;
        return this;
    }

    public CashRegister build() {
        CashRegister cashRegister = new CashRegister();
        cashRegister.setId(this.id);
        cashRegister.setDate(this.date);
        cashRegister.setReport(this.report);
        cashRegister.setApartment(this.apartment);
        cashRegister.setCashIn(this.cashIn);
        cashRegister.setCashOut(this.cashOut);
        cashRegister.setTotal(this.total);
        cashRegister.setHour(this.hour);
        return cashRegister;
    }
}