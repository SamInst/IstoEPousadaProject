package com.example.PousadaIstoE.response;

public record ClientResponse(
        String name,
        String CPF,
        String phone,
        String address,
        String job,
        String registeredBy
){}
