package com.example.PousadaIstoE.response;

import java.time.LocalTime;

public record EntradaResponse(
        Integer apartamento,
        LocalTime hora_entrada,
        LocalTime hora_saida,
        String consumo,
        String placa,
        Float total
) {
}
