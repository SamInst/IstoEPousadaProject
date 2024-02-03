package com.example.PousadaIstoE.builders;

import com.example.PousadaIstoE.Enums.PaymentType;
import com.example.PousadaIstoE.model.Client;
import com.example.PousadaIstoE.model.OvernightStayCompanion;
import com.example.PousadaIstoE.model.OvernightStayReservation;

import java.time.LocalDate;
import java.util.List;

public class ReservationBuilder {

    private Long id;
    private LocalDate startDate;
    private LocalDate endDate;
    private Client client;
    private List<OvernightStayCompanion> companionList;
    private Integer room;
    private PaymentType paymentType;

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
    public ReservationBuilder client(Client client) {
        this.client = client;
        return this;
    }

    public ReservationBuilder companions(List<OvernightStayCompanion> companionList) {
        this.companionList = companionList;
        return this;
    }
    public ReservationBuilder room(Integer room) {
        this.room = room;
        return this;
    }


    public ReservationBuilder paymentType(PaymentType paymentType) {
        this.paymentType = paymentType;
        return this;
    }

    public OvernightStayReservation build(){
        return new OvernightStayReservation(
                id,
                startDate,
                endDate,
                client,
                companionList,
                room,
                paymentType);
    }
}
