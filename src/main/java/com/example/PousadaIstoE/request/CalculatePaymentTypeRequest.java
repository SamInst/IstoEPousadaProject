package com.example.PousadaIstoE.request;

import com.example.PousadaIstoE.Enums.PaymentStatus;

public record CalculatePaymentTypeRequest(
        Long payment_type_id,
        Float value,
        PaymentStatus status
) {}
