package com.example.PousadaIstoE.response;

public record ClientResponse(
        Long id,
        String name,
        String CPF,
        String phone,
        String address,
        String job,
        String registeredBy,
        Boolean active
){}
