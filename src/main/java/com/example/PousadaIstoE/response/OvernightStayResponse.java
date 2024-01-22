package com.example.PousadaIstoE.response;

import com.example.PousadaIstoE.Enums.PaymentStatus;
import com.example.PousadaIstoE.Enums.PaymentType;
import com.example.PousadaIstoE.model.OverNightStayConsumption;

import java.time.LocalDate;
import java.util.List;

public record OvernightStayResponse(
        Long id,
        Client client,
        List<OvernightStayCompanionShortResponse> companion,
        Rooms room,
        LocalDate start_date,
        LocalDate end_date,
        List<OverNightStayConsumption> consumptionList,
        Values values
) {
    public record Client (
        String name,
        String cpf,
        String phone_number
    ){}

    public record Rooms(
        Integer number
    ){}
    public record Values(
            Integer amount_people,
            Integer amount_days,
            Float daily_value,
            Double total_consumption,
            Float total_daily,
            PaymentType payment_type,
            PaymentStatus payment_status,
            Double total_value
    ){}
}