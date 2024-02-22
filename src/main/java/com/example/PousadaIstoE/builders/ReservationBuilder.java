package com.example.PousadaIstoE.builders;

import com.example.PousadaIstoE.Enums.PaymentStatus;
import com.example.PousadaIstoE.model.Customer;
import com.example.PousadaIstoE.model.OvernightStayReservation;
import com.example.PousadaIstoE.model.PaymentType;

import java.time.LocalDate;
import java.util.List;

public class ReservationBuilder {
    private Long id;
    private LocalDate startDate;
    private LocalDate endDate;
    private List<Customer> customerList;
    private Integer room;
    private List<PaymentType> paymentType;
    private PaymentStatus paymentStatus;
    private Boolean isActive;
    private String obs;

    public ReservationBuilder id(Long id) {
        this.id = id;
        return this;
    }

    public ReservationBuilder startDate(LocalDate startDate) {
        this.startDate = startDate;
        return this;
    }

    public ReservationBuilder endDate(LocalDate endDate) {
        this.endDate = endDate;
        return this;
    }
    public ReservationBuilder clientList(List<Customer> customerList) {
        this.customerList = customerList;
        return this;
    }

    public ReservationBuilder room(Integer room) {
        this.room = room;
        return this;
    }

    public ReservationBuilder paymentType(List<PaymentType> paymentType) {
        this.paymentType = paymentType;
        return this;
    }

    public ReservationBuilder paymentStatus(PaymentStatus paymentStatus) {
        this.paymentStatus = paymentStatus;
        return this;
    }

    public ReservationBuilder isActive(Boolean isActive) {
        this.isActive = isActive;
        return this;
    }
    public ReservationBuilder obs(String obs) {
        this.obs = obs;
        return this;
    }

    public OvernightStayReservation build(){
        return new OvernightStayReservation(
                id,
                startDate,
                endDate,
                customerList,
                room,
                paymentType,
                paymentStatus,
                isActive,
                obs);
    }
}
