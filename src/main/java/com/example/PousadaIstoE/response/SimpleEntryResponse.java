package com.example.PousadaIstoE.response;

import com.example.PousadaIstoE.Enums.EntryStatus;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public record SimpleEntryResponse(
        Long id,
        Integer apartment,
        @JsonFormat(pattern = "HH:mm") LocalDateTime hour_entry,
        @JsonFormat(pattern = "HH:mm") LocalDateTime hour_out,
        String plate,
        EntryStatus entryStatus
){}