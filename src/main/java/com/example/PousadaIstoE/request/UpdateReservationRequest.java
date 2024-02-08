package com.example.PousadaIstoE.request;

import com.example.PousadaIstoE.Enums.PaymentType;

import java.time.LocalDate;
import java.util.List;

public record UpdateReservationRequest(
        List<ClientRequest> clientList,
        LocalDate startDate,
        LocalDate endDate,
        Integer room,
        PaymentType paymentType
){}
