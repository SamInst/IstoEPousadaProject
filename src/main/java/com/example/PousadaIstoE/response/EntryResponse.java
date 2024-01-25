package com.example.PousadaIstoE.response;

import com.example.PousadaIstoE.Enums.EntryStatus;

import java.time.LocalTime;
import java.util.List;

public record EntryResponse(
        Integer apartamento,
        LocalTime hora_entrada,
        LocalTime hora_saida,
        String licensePlate,
        TempoPermanecido tempo_permanecido,
        List<ConsumptionResponse> consumo,
        EntryStatus entryStatus,

        double total_consumo,
        double valor_entrada,
        double total
) {
    public record TempoPermanecido(
            int horas,
            int minutos
    ){}
}
