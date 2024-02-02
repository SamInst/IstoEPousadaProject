package com.example.PousadaIstoE.request;

import java.time.LocalDate;

public record CompanionRequest(
        String name,
        String cpf,
        LocalDate birth
){}
