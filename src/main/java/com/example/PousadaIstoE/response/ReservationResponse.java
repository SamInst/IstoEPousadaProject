package com.example.PousadaIstoE.response;

import com.example.PousadaIstoE.Enums.PaymentType;

import java.time.LocalDate;
import java.util.List;

public record ReservationResponse(
    Long id,
    ClientResponse client,
    List<CompanionResponse> companionList,
    LocalDate startDate,
    LocalDate endDate,
    Integer room,
    PaymentType paymentType
){}
