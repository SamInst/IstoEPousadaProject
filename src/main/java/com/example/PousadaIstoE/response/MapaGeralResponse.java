package com.example.PousadaIstoE.response;

import java.time.LocalDate;
import java.time.LocalTime;

public record MapaGeralResponse(
        LocalDate data,
        LocalTime hora,
        String relatorio,
        Integer apartamento,
        Float entrada,
        Float saida,
        Float total

) {
}
