package com.example.PousadaIstoE.model;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

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

    public CashRegister() {}
    public Long getId() {return id;}
    public void setId(Long id) {this.id = id;}
    public LocalDate getDate() {return date;}
    public void setDate(LocalDate date) {this.date = date;}
    public String getReport() {return report;}
    public void setReport(String report) {this.report = report;}
    public Integer getApartment() {return apartment;}
    public void setApartment(Integer apartment) {this.apartment = apartment;}
    public Float getCashIn() {return cashIn;}
    public void setCashIn(Float cashIn) {this.cashIn = cashIn;}
    public Float getCashOut() {return cashOut;}
    public void setCashOut(Float cashOut) {this.cashOut = cashOut;}
    public Float getTotal() {return total;}
    public void setTotal(Float total) {this.total = total;}
    public LocalTime getHour() {return hour;}
    public void setHour(LocalTime hour) {this.hour = hour;}
}