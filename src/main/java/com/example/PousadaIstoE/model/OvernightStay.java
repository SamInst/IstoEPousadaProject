package com.example.PousadaIstoE.model;

import com.example.PousadaIstoE.Enums.PaymentStatus;
import com.example.PousadaIstoE.Enums.PaymentType;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.List;

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

    @OneToMany
    @JoinColumn(name = "fkip08ip01_client_list")
    private List<Client> clientList;

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

    @Column(name = "ip08_total_consumption")
    private Float totalConsumption;
    @Column(name = "ip08_total")
    private Float total;

    @Column(name = "ip08_active")
    private boolean isActive;

    public Float getTotal() {
        return total;
    }

    public void setTotal(Float total) {
        this.total = total;
    }

    public void setClientList(List<Client> clientList) {
        this.clientList = clientList;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Rooms getRoom() {
        return room;
    }

    public void setRoom(Rooms room) {
        this.room = room;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public Integer getAmountPeople() {
        return amountPeople;
    }

    public void setAmountPeople(Integer amountPeople) {
        this.amountPeople = amountPeople;
    }

    public PaymentType getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(PaymentType paymentType) {
        this.paymentType = paymentType;
    }

    public PaymentStatus getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(PaymentStatus paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public List<Client> getClientList() {
        return clientList;
    }

    public Float getTotalConsumption() {
        return totalConsumption;
    }

    public void setTotalConsumption(Float totalConsumption) {
        this.totalConsumption = totalConsumption;
    }

    public OvernightStay(Long id, Rooms room, List<Client> clientList, LocalDate startDate, LocalDate endDate, Integer amountPeople, PaymentType paymentType, PaymentStatus paymentStatus, Float totalConsumption, Float total, boolean isActive) {
        this.id = id;
        this.room = room;
        this.clientList = clientList;
        this.startDate = startDate;
        this.endDate = endDate;
        this.amountPeople = amountPeople;
        this.paymentType = paymentType;
        this.paymentStatus = paymentStatus;
        this.totalConsumption = totalConsumption;
        this.total = total;
        this.isActive = isActive;
    }

    public OvernightStay() {
    }
}