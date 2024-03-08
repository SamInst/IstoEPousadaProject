package com.example.PousadaIstoE.request;

import com.example.PousadaIstoE.Enums.PaymentStatus;

public record PaymentRequest(
        Long payment_type_id,
        Float value,
        PaymentStatus status
) {}
