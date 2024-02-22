package com.example.PousadaIstoE.response;

public record CustomerResponse(
        Long id,
        String name,
        String CPF,
        String phone,
        String address,
        String job,
        String country,
        String state,
        String county,
        String registered_by,
        Boolean is_hosted
){}
