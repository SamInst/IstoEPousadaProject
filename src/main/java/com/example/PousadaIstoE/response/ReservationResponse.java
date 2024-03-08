package com.example.PousadaIstoE.response;

import java.time.LocalDate;
import java.util.List;

public record ReservationResponse(
    Long id,
    List<CustomerResponse> client_response_list,
    LocalDate start_date,
    LocalDate end_date,
    Integer room,
    List<PaymentResponse> payment_type,
    Integer amount_people,
    String obs,
    Boolean isActive,
    Float total
){}
