package com.example.PousadaIstoE.response;

import java.time.LocalTime;
import java.util.List;

public record EntradaResponse(
        Integer apartamento,
        LocalTime hora_entrada,
        LocalTime hora_saida,
        String placa,
        TempoPermanecido tempo_permanecido,
        java.util.List<java.util.List<com.example.PousadaIstoE.model.EntradaConsumo>> consumo,
        double total
) {
    public record TempoPermanecido(
            int horas,
            int minutos
    ){}


//    public record EntradaConsumo(
//            String Item,
//            Float valor
//    ){}
}
