package com.example.PousadaIstoE.response;

import java.time.LocalDate;

public record SimpleOvernightResponse(
    Long id,
    String client_name,
    Integer apartment,
    LocalDate start_date,
    LocalDate end_date,
    Integer amount_people

){}
