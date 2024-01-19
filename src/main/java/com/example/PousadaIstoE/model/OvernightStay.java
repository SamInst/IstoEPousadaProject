package com.example.PousadaIstoE.model;

import com.example.PousadaIstoE.response.PaymentStatus;
import com.example.PousadaIstoE.response.PaymentType;
import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "ip08_overnight_stay")
public class OvernightStay {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ip08_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "fkip08ip04_rooms_id")
    private Rooms room;

    @OneToOne
    @JoinColumn(name = "fkip08ip01_client_id")
    private Client client;

    @Column(name = "ip08_start_date")
    private LocalDate startDate;

    @Column(name = "ip08_end_date")
    private LocalDate endDate;

    @Column(name = "ip08_amount_people")
    private Integer amountPeople;

    @Column(name = "ip08_payment_type")
    @Enumerated(EnumType.STRING)
    private PaymentType paymentType;

    @Column(name = "ip08_payment_status")
    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;

    @Column(name = "ip08_total")
    private Float total;


    public Float getTotal() {
        return total;
    }
    public void setTotal(Float total) {
        this.total = total;
    }
    public Long getId() {
        return id;
    }
    public void setId(Long id) {this.id = id;}
    public Integer getAmountPeople() {return amountPeople;}
    public void setAmountPeople(Integer amountPeople) {this.amountPeople = amountPeople;}
    public LocalDate getStartDate() {
        return startDate;
    }
    public LocalDate getEndDate() {
        return endDate;
    }
    public Client getClient() {
        return client;
    }
    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }
    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }
    public void setClient(Client client) {
        this.client = client;
    }
    public PaymentType getTipoPagamento() {
        return paymentType;
    }
    public PaymentStatus getPaymentStatus() {
        return paymentStatus;
    }
    public Rooms getRoom() {
        return room;
    }
    public void setRoom(Rooms room) {
        this.room = room;
    }
    public void setTipoPagamento(PaymentType paymentType) {
        this.paymentType = paymentType;
    }
    public void setPaymentStatus(PaymentStatus paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public OvernightStay(Long id, Rooms room, Client client, LocalDate startDate, LocalDate endDate, Integer amountPeople, PaymentType paymentType, PaymentStatus paymentStatus, Float total) {
        this.id = id;
        this.room = room;
        this.client = client;
        this.startDate = startDate;
        this.endDate = endDate;
        this.amountPeople = amountPeople;
        this.paymentType = paymentType;
        this.paymentStatus = paymentStatus;
        this.total = total;
    }

    public OvernightStay() {
    }
}