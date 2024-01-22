package com.example.PousadaIstoE.response;

import java.time.LocalDate;

public record OvernightStayShortResponse(
    Long id,
    Client client,
    Integer apartment,
    LocalDate start_date,
    LocalDate end_date,
    Integer amount_people

){
    public record Client (
        String client_name
    ){}
}
