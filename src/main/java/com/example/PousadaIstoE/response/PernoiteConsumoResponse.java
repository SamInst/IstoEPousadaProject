package com.example.PousadaIstoE.response;

public record PernoiteConsumoResponse(
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
