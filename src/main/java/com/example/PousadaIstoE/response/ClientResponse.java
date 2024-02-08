package com.example.PousadaIstoE.response;

public record ClientResponse(
        Long id,
        String name,
        String CPF,
        String phone,
        String address,
        String job,
        String registered_by,
        Boolean is_hosted
){}
