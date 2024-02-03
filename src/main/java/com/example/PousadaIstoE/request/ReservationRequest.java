package com.example.PousadaIstoE.request;

import com.example.PousadaIstoE.Enums.PaymentType;

import java.time.LocalDate;
import java.util.List;

public record ReservationRequest(
        ClientRequest client,
        List<CompanionRequest> companions,
        LocalDate startDate,
        LocalDate endDate,
        Integer room,
        PaymentType paymentType
){}
