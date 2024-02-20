package com.example.PousadaIstoE.response;

import com.example.PousadaIstoE.Enums.PaymentStatus;
import com.example.PousadaIstoE.Enums.PaymentType;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

public record ReservationResponse(
    Long id,
    List<ClientResponse> client_response_list,
    LocalDate start_date,
    LocalDate end_date,
    Integer room,
    PaymentType payment_type,
    PaymentStatus payment_status,
    Integer amount_people,
    String obs,
    Boolean isActive,
    Float total
){}
