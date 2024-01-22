package com.example.PousadaIstoE.response;

public record OvernightStayConsumptionResponse(
        Integer amount,
        Item item,
        Float total
){
    public record Item (
            String description,
            Float value,
            Float subTotal
    ){}
}