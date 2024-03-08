package com.example.PousadaIstoE.request;

import java.time.LocalDate;
import java.util.List;

public record ReservationRequest(
        List<ConsumerRequest> clients,
        LocalDate start_date,
        LocalDate end_date,
        Integer room,
        List<PaymentRequest> paymentType,
        String obs
){}
