package com.example.PousadaIstoE.request;

import com.example.PousadaIstoE.Enums.PaymentStatus;

import java.time.LocalDate;
import java.util.List;

public record UpdateReservationRequest(
        List<ConsumerRequest> clients,
        LocalDate start_date,
        LocalDate end_date,
        Integer room,
        String obs
){}
