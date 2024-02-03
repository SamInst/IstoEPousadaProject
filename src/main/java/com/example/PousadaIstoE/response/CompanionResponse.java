package com.example.PousadaIstoE.response;

import java.time.LocalDate;

public record CompanionResponse(
    Long id,
    String name,
    String cpf,
    LocalDate birth,
    Integer age
){}
