package com.example.PousadaIstoE.request;

import com.example.PousadaIstoE.Enums.PaymentStatus;
import com.example.PousadaIstoE.Enums.PaymentType;
import java.time.LocalDate;
import java.util.List;

public record UpdateReservationRequest(
        List<ClientRequest> clients,
        LocalDate start_date,
        LocalDate end_date,
        Integer room,
        PaymentType payment_type,
        PaymentStatus payment_status
){}
