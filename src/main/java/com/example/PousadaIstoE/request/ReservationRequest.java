package com.example.PousadaIstoE.request;

import java.time.LocalDate;
import java.util.List;

public record ReservationRequest(
        ClientRequest client,
        List<CompanionRequest> companions,
        LocalDate startDate,
        LocalDate endDate,
        Integer room
){}
