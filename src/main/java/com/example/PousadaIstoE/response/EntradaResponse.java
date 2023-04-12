package com.example.PousadaIstoE.response;

import java.time.LocalTime;

public record EntradaResponse(
        Integer apartamento,
        LocalTime hora_entrada,
        LocalTime hora_saida,
        String consumo,
        String placa,
        TempoPermanecido tempo_permanecido,
        double total
) {
    public record TempoPermanecido(
            int horas,
            int minutos
    ){}
}
