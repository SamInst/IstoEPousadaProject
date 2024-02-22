package com.example.PousadaIstoE.response;

public record ConsumptionResponse(
        Long id,
        Integer amount,
        String item,
        Float value,
        Float total
) {
}
