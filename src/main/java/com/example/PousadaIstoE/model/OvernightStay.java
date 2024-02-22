package com.example.PousadaIstoE.model;

import com.example.PousadaIstoE.Enums.OvernightStayStatus;
import com.example.PousadaIstoE.Enums.PaymentStatus;
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
    private List<Customer> customerList;

    @Column(name = "ip08_start_date")
    private LocalDate startDate;

    @Column(name = "ip08_end_date")
    private LocalDate endDate;

    @Column(name = "ip08_amount_people")
    private Integer amountPeople;

    @OneToMany
    @JoinColumn(name = "ip08_payment_type")
    private List<PaymentType> paymentType;

    @Column(name = "ip08_payment_status")
    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;

    @Column(name = "ip08_total_consumption")
    private Float totalConsumption;

    @Column(name = "ip08_overnight_value")
    private Float overnightValue;
    @Column(name = "ip08_total")
    private Float total;

    @Column(name = "ip08_active")
    private boolean isActive;

    @Column(name = "ip08_obs")
    private String obs;

    @Column(name = "ip08_overnight_status")
    private OvernightStayStatus overnightStayStatus;

    public Float getTotal() {
        return total;
    }

    public void setTotal(Float total) {
        this.total = total;
    }

    public void setClientList(List<Customer> customerList) {
        this.customerList = customerList;
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



    public PaymentStatus getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(PaymentStatus paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public List<Customer> getClientList() {
        return customerList;
    }

    public Float getTotalConsumption() {
        return totalConsumption;
    }

    public Float getOvernightValue() {
        return overnightValue;
    }

    public void setOvernightValue(Float overnightValue) {
        this.overnightValue = overnightValue;
    }

    public void setTotalConsumption(Float totalConsumption) {
        this.totalConsumption = totalConsumption;
    }

    public String getObs() {
        return obs;
    }

    public OvernightStayStatus getOvernightStayStatus() {
        return overnightStayStatus;
    }

    public void setOvernightStayStatus(OvernightStayStatus overnightStayStatus) {
        this.overnightStayStatus = overnightStayStatus;
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

    public OvernightStay(Long id, Rooms room, List<Customer> customerList, LocalDate startDate, LocalDate endDate, Integer amountPeople, List<PaymentType> paymentType, PaymentStatus paymentStatus, Float totalConsumption, Float overnightValue, Float total, boolean isActive, String obs, OvernightStayStatus overnightStayStatus) {
        this.id = id;
        this.room = room;
        this.customerList = customerList;
        this.startDate = startDate;
        this.endDate = endDate;
        this.amountPeople = amountPeople;
        this.paymentType = paymentType;
        this.paymentStatus = paymentStatus;
        this.totalConsumption = totalConsumption;
        this.overnightValue = overnightValue;
        this.total = total;
        this.isActive = isActive;
        this.obs = obs;
        this.overnightStayStatus = overnightStayStatus;
    }

    public OvernightStay() {
    }
}