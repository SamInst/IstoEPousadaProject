package com.example.PousadaIstoE.response;

public record ConsumptionResponse(
        Integer quantidade,
        String item,
        Float valor,
        Float total
) {
}
