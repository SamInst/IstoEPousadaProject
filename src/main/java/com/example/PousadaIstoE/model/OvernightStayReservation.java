package com.example.PousadaIstoE.model;

import com.example.PousadaIstoE.Enums.PaymentStatus;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "ip13_reservations")
public class OvernightStayReservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ip13_id_reservations")
    private Long id;

    @Column(name = "ip13_start_date")
    private LocalDate startDate;

    @Column(name = "ip13_end_date")
    private LocalDate endDate;

    @ManyToMany
    @JoinColumn(name = "fkip13ip01_id_customer")
    private List<Customer> customer;

    @Column(name = "ip13_room")
    private Integer room;

    @OneToMany
    @JoinColumn(name = "ip13_payment_type")
    private List<PaymentType> paymentType;

    @Column(name = "ip13_payment_status")
    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;

    @Column(name = "ip13_is_active")
    private Boolean isActive;

    @Column(name = "ip13_obs")
    private String obs;

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public List<Customer> getClient() {
        return customer;
    }

    public void setClient(List<Customer> customer) {
        this.customer = customer;
    }

    public Integer getRoom() {
        return room;
    }

    public void setRoom(Integer room) {
        this.room = room;
    }

    public PaymentStatus getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(PaymentStatus paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getObs() {
        return obs;
    }

    public void setObs(String obs) {
        this.obs = obs;
    }

    public List<PaymentType> getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(List<PaymentType> paymentType) {
        this.paymentType = paymentType;
    }

    public OvernightStayReservation() {
    }

    public OvernightStayReservation(Long id, LocalDate startDate, LocalDate endDate, List<Customer> customer, Integer room, List<PaymentType> paymentType, PaymentStatus paymentStatus, Boolean isActive, String obs) {
        this.id = id;
        this.startDate = startDate;
        this.endDate = endDate;
        this.customer = customer;
        this.room = room;
        this.paymentType = paymentType;
        this.paymentStatus = paymentStatus;
        this.isActive = isActive;
        this.obs = obs;
    }
}
