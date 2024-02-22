package com.example.PousadaIstoE.response;

import com.example.PousadaIstoE.Enums.PaymentStatus;

import java.time.LocalDate;
import java.util.List;

public record ReservationResponse(
    Long id,
    List<ConsumerResponse> client_response_list,
    LocalDate start_date,
    LocalDate end_date,
    Integer room,
    List<PaymentTypeResponse> payment_type,
    PaymentStatus payment_status,
    Integer amount_people,
    String obs,
    Boolean isActive,
    Float total
){}
