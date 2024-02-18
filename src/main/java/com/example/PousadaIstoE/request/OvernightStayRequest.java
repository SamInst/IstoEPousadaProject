package com.example.PousadaIstoE.request;

import com.example.PousadaIstoE.Enums.PaymentStatus;
import com.example.PousadaIstoE.Enums.PaymentType;

import java.time.LocalDate;
import java.util.List;

public record OvernightStayRequest(
        Long room_id,
        List<ClientRequest> client_list,
        LocalDate start_date,
        LocalDate end_date,
        PaymentType payment_type,
        PaymentStatus payment_status
){}
