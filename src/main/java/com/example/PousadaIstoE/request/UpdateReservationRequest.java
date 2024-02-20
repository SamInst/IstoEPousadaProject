package com.example.PousadaIstoE.request;

import com.example.PousadaIstoE.Enums.PaymentStatus;
import com.example.PousadaIstoE.Enums.PaymentType;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

public record UpdateReservationRequest(
        List<ClientRequest> clients,
        LocalDate start_date,
        LocalDate end_date,
        Integer room,
        Set<PaymentType> payment_type,
        PaymentStatus payment_status,
        String obs
){}
