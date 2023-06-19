package com.example.PousadaIstoE.response;

public record ConsumoResponse(
        Integer quantidade,
        String item,
        Float valor,
        Float total
) {
}
