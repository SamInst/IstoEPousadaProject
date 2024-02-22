package com.example.PousadaIstoE.request;

import com.example.PousadaIstoE.Enums.OvernightStayStatus;
import com.example.PousadaIstoE.Enums.PaymentStatus;

import java.time.LocalDate;
import java.util.List;

public record OvernightStayRequest(
        Long room_id,
        List<ConsumerRequest> client_list,
        LocalDate start_date,
        LocalDate end_date,
        List<Long> payment_type_id,
        PaymentStatus payment_status,
        String obs,
        OvernightStayStatus overnightStatus
){}
