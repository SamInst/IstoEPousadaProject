package com.example.PousadaIstoE.response;

import com.example.PousadaIstoE.Enums.PaymentStatus;
import com.example.PousadaIstoE.Enums.PaymentType;

import java.time.LocalDate;
import java.util.List;

public record ReservationResponse(
    Long id,
    List<ClientResponse> clientResponseList,
    LocalDate startDate,
    LocalDate endDate,
    Integer room,
    PaymentType paymentType,
    PaymentStatus paymentStatus
){}
