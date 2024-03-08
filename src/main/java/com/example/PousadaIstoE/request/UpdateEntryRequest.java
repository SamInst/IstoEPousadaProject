package com.example.PousadaIstoE.request;

import com.example.PousadaIstoE.Enums.EntryStatus;
import com.example.PousadaIstoE.Enums.PaymentStatus;
import com.example.PousadaIstoE.model.PaymentType;

import java.util.List;

public record UpdateEntryRequest(
        Long room_id,
        String vehicle_plate,
        EntryStatus entry_status,
        String obs
){}
