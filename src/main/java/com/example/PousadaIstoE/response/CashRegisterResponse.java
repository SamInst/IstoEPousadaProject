package com.example.PousadaIstoE.response;

import java.time.LocalDate;
import java.time.LocalTime;

public record CashRegisterResponse(
        LocalDate date,
        LocalTime hora,
        String relatorio,
        Integer apartamento,
        Float entrada,
        Float saida,
        Float total

) {
}
