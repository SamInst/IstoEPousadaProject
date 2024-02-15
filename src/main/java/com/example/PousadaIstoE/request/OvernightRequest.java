package com.example.PousadaIstoE.request;

import com.example.PousadaIstoE.Enums.PaymentType;

import java.time.LocalDate;
import java.util.List;

public record OvernightRequest(
        List<ClientRequest> clients,
        Long client_id,
        LocalDate startDate,
        LocalDate endDate,
        PaymentType paymentType
) {
}
