package com.example.PousadaIstoE.response;

import java.time.LocalDate;
import java.util.List;

public record SimpleOvernightResponse(
    Long id,
    List<String> client_name,
    Integer apartment,
    LocalDate start_date,
    LocalDate end_date,
    Integer amount_people

){}
