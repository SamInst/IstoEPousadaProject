package com.example.PousadaIstoE.request;

import java.time.LocalDate;

public record ConsumerRequest(
        String name,
        String cpf,
        String phone,
        String email,
        LocalDate birth,
        String address,
        String job,
        Long country_id,
        Long state_id,
        Long county_id,
        String obs
){}
