package com.example.PousadaIstoE.response;

import java.time.LocalDate;
import java.time.LocalTime;

public record CashRegisterResponse(
        Long id,
        LocalDate date,
        LocalTime hour,
        String report,
        Integer apartment,
        Float cashIn,
        Float cashOut,
        Float total

) {
}
