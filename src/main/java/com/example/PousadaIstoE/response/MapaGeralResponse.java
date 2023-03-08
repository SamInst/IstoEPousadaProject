package com.example.PousadaIstoE.response;

import java.time.LocalDate;

public record MapaGeralResponse(
        LocalDate data,
        String relatorio,
        Integer apartamento,
        Float entrada,
        Float saida,
        Float total

) {
}
