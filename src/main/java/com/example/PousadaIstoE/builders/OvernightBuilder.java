package com.example.PousadaIstoE.builders;

import com.example.PousadaIstoE.Enums.OvernightStayStatus;
import com.example.PousadaIstoE.Enums.PaymentStatus;
import com.example.PousadaIstoE.Enums.PaymentType;
import com.example.PousadaIstoE.model.Client;
import com.example.PousadaIstoE.model.OvernightStay;
import com.example.PousadaIstoE.model.Rooms;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

public class OvernightBuilder {
    private Long id;
    private Rooms room;
    private List<Client> clientList;
    private LocalDate startDate;
    private LocalDate endDate;
    private Integer amountPeople;
    Set<PaymentType> paymentType;
    private PaymentStatus paymentStatus;
    private Float totalConsumption;
    private Float total;
    private Float overnightValue;
    private boolean isActive;
    private String obs;
    private OvernightStayStatus overnightStayStatus;


    public OvernightBuilder id(Long id) {
        this.id = id;
        return this;
    }

    public OvernightBuilder room(Rooms room) {
        this.room = room;
        return this;
    }

    public OvernightBuilder clientList(List<Client> clientList) {
        this.clientList = clientList;
        return this;
    }

    public OvernightBuilder startDate(LocalDate startDate) {
        this.startDate = startDate;
        return this;
    }

    public OvernightBuilder endDate(LocalDate endDate) {
        this.endDate = endDate;
        return this;
    }

    public OvernightBuilder amountPeople(Integer amountPeople) {
        this.amountPeople = amountPeople;
        return this;
    }

    public OvernightBuilder paymentType(Set<PaymentType> paymentType) {
        this.paymentType = paymentType;
        return this;
    }

    public OvernightBuilder paymentStatus(PaymentStatus paymentStatus) {
        this.paymentStatus = paymentStatus;
        return this;
    }

    public OvernightBuilder totalConsumption(Float totalConsumption) {
        this.totalConsumption = totalConsumption;
        return this;
    }

    public OvernightBuilder total(Float total) {
        this.total = total;
        return this;
    }

    public OvernightBuilder overnightValue(Float overnightValue) {
        this.overnightValue = overnightValue;
        return this;
    }

    public OvernightBuilder isActive(boolean isActive) {
        this.isActive = isActive;
        return this;
    }

    public OvernightBuilder obs(String obs) {
        this.obs = obs;
        return this;
    }

    public OvernightBuilder status(OvernightStayStatus overnightStayStatus) {
        this.overnightStayStatus = overnightStayStatus;
        return this;
    }

    public OvernightStay build() {
        return new OvernightStay(
                id,
                room,
                clientList,
                startDate,
                endDate,
                amountPeople,
                paymentType,
                paymentStatus,
                totalConsumption,
                overnightValue,
                total,
                isActive,
                obs,
                overnightStayStatus
        );
    }
}
