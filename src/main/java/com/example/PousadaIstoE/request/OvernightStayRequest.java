package com.example.PousadaIstoE.request;

import com.example.PousadaIstoE.Enums.OvernightStayStatus;
import com.example.PousadaIstoE.Enums.PaymentStatus;

import java.time.LocalDate;
import java.util.List;

public record OvernightStayRequest(
        Long room_id,
        List<ConsumerRequest> customer_list,
        LocalDate start_date,
        LocalDate end_date,
        List<PaymentRequest> payment_type,
        PaymentStatus payment_status,
        String obs,
        OvernightStayStatus overnightStatus
){}
