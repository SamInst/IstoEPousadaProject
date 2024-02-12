package com.example.PousadaIstoE.request;

import com.example.PousadaIstoE.Enums.EntryStatus;
import com.example.PousadaIstoE.Enums.PaymentStatus;
import com.example.PousadaIstoE.Enums.PaymentType;

public record UpdateEntryRequest(
        Long room_id,
        String vehicle_plate,
        EntryStatus entry_status,
        PaymentType payment_type,
        PaymentStatus payment_status
){}
