package com.example.PousadaIstoE.response;

import com.example.PousadaIstoE.Enums.EntryStatus;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public record EntryResponse(
        Long id,
        Integer apartment_number,
        LocalDate date_register,
        @JsonFormat(pattern = "HH:mm") LocalDateTime entry_time,
        @JsonFormat(pattern = "HH:mm") LocalDateTime departure_time,
        String vehicle_plate,
        String time_spent,
        List<ConsumptionResponse> consumptionResponseList,
        EntryStatus entry_status,
        List<PaymentResponse> paymentType,
        double total_consumption,
        double entry_value,
        double total
){}
