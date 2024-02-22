package com.example.PousadaIstoE.request;

import com.example.PousadaIstoE.Enums.PaymentStatus;

import java.time.LocalDate;
import java.util.List;

public record ReservationRequest(
        List<ConsumerRequest> clients,
        LocalDate start_date,
        LocalDate end_date,
        Integer room,
        List<Long> payment_type_id,
        PaymentStatus payment_status,
        String obs
){}
