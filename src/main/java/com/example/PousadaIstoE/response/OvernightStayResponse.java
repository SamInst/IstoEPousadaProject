package com.example.PousadaIstoE.response;

import com.example.PousadaIstoE.model.OverNightStayConsumption;

import java.time.LocalDate;
import java.util.List;

public record OvernightStayResponse(
        Long id,
        Client cliente,
        List<AcompanhantePernoiteShortResponse> acompanhantes,
        Rooms rooms,
        LocalDate data_de_entrada,
        LocalDate data_de_saida,
        List<OverNightStayConsumption> consumo,
        Valores valores
) {
    public record Client (
        String name,
        String cpf,
        String phone_number
    ){}

    public record Rooms(
        Integer number
    ){}
    public record Valores(
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