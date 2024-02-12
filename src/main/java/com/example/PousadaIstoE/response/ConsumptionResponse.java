package com.example.PousadaIstoE.response;

public record ConsumptionResponse(
        Integer amount,
        String item,
        Float value,
        Float total
) {
}
