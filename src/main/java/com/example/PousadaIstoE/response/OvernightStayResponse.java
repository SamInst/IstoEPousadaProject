package com.example.PousadaIstoE.response;

import com.example.PousadaIstoE.Enums.PaymentStatus;
import com.example.PousadaIstoE.Enums.RoomType;
import com.example.PousadaIstoE.request.CalculatePaymentTypeRequest;

import java.time.LocalDate;
import java.util.List;

public record OvernightStayResponse(
        Long id,
        List<CustomerResponse> clients,
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
            List<CalculatePaymentTypeResponse> payment_type,
            PaymentStatus payment_status,
            Float total_value
    ){}
    public record Room(
            Integer number,
            RoomType roomType
    ){}
}