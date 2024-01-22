package com.example.PousadaIstoE.response;

import com.example.PousadaIstoE.Enums.EntryStatus;

import java.time.LocalTime;

public record SimpleEntryResponse(
        Long id,
        Integer apartamento,
        LocalTime hora_entrada,
        LocalTime hora_saida,
        String placa,
        EntryStatus entryStatus
){}