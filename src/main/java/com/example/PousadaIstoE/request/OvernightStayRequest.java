package com.example.PousadaIstoE.request;

import com.example.PousadaIstoE.Enums.OvernightStayStatus;
import com.example.PousadaIstoE.Enums.PaymentStatus;
import com.example.PousadaIstoE.Enums.PaymentType;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

public record OvernightStayRequest(
        Long room_id,
        List<ClientRequest> client_list,
        LocalDate start_date,
        LocalDate end_date,
        Set<PaymentType> payment_type,
        PaymentStatus payment_status,
        String obs,
        OvernightStayStatus overnightStatus
){}
