package com.example.PousadaIstoE.response;

public record EntryConsumptionResponse(
        Integer quantidade,
        Item item,
        Float total
){
    public record Item (
            String descricao,
            Float valor,
            Float subTotal
    ){}
}
