package com.example.PousadaIstoE.request;

public record CalculatePaymentTypeRequest(
        Long payment_type_id,
        Float value
) {}
