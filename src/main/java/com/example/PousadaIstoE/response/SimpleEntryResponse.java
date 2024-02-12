package com.example.PousadaIstoE.response;

import com.example.PousadaIstoE.Enums.EntryStatus;

import java.time.LocalTime;

public record SimpleEntryResponse(
        Long id,
        Integer apartment,
        LocalTime hour_entry,
        LocalTime hour_out,
        String plate,
        EntryStatus entryStatus
){}