package com.example.PousadaIstoE.request;

import com.example.PousadaIstoE.model.PaymentType;

import java.time.LocalDate;
import java.util.List;

public record OvernightRequest(
        List<ConsumerRequest> clients,
        Long client_id,
        LocalDate startDate,
        LocalDate endDate,
        List<PaymentType> paymentType
) {
}
