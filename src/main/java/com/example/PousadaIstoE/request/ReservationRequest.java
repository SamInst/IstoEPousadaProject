package com.example.PousadaIstoE.request;

import com.example.PousadaIstoE.Enums.PaymentType;

import java.time.LocalDate;
import java.util.List;

public record ReservationRequest(
        List<ClientRequest> client,
        LocalDate startDate,
        LocalDate endDate,
        Integer room,
        PaymentType paymentType
){}
