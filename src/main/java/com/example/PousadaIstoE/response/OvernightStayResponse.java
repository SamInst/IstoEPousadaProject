package com.example.PousadaIstoE.response;

import com.example.PousadaIstoE.Enums.PaymentStatus;
import com.example.PousadaIstoE.Enums.PaymentType;
import com.example.PousadaIstoE.Enums.RoomType;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

public record OvernightStayResponse(
        Long id,
        List<ClientResponse> clients,
        Room room,
        LocalDate start_date,
        LocalDate end_date,
        List<ConsumptionResponse> consumptions,
        Values values,
        String obs
){
    public record Values(
            Integer amount_people,
            Integer amount_days,
            Float total_consumption,
            Float daily_value,
            Set<PaymentType> payment_type,
            PaymentStatus payment_status,
            Float total_value
    ){}
    public record Room(
            Integer number,
            RoomType roomType
    ){}
}