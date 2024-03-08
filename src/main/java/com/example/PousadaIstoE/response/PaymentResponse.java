package com.example.PousadaIstoE.response;

import com.example.PousadaIstoE.Enums.PaymentStatus;

public record PaymentResponse(
        Long id,
        String paymentType,
        Float value,
        PaymentStatus status
){}
