package com.example.PousadaIstoE.model;

import com.example.PousadaIstoE.Enums.PaymentStatus;
import com.example.PousadaIstoE.Enums.PaymentType;
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

    @OneToMany
    @JoinColumn(name = "fkip13ip01_id_client")
    private List<Client> clientList;

    @Column(name = "ip13_room")
    private Integer room;

    @Column(name = "ip13_payment_type")
    @Enumerated(EnumType.STRING)
    private PaymentType paymentType;

    @Column(name = "ip13_payment_status")
    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;

    @Column(name = "ip13_is_active")
    private Boolean isActive;

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

    public List<Client> getClientList() {
        return clientList;
    }

    public void setClientList(List<Client> clientList) {
        this.clientList = clientList;
    }

    public Integer getRoom() {
        return room;
    }

    public void setRoom(Integer room) {
        this.room = room;
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

    public OvernightStayReservation() {
    }

    public OvernightStayReservation(Long id, LocalDate startDate, LocalDate endDate, List<Client> clientList, Integer room, PaymentType paymentType, PaymentStatus paymentStatus, Boolean isActive) {
        this.id = id;
        this.startDate = startDate;
        this.endDate = endDate;
        this.clientList = clientList;
        this.room = room;
        this.paymentType = paymentType;
        this.paymentStatus = paymentStatus;
        this.isActive = isActive;
    }
}
