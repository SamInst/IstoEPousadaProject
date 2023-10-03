package com.example.PousadaIstoE.response;

import com.example.PousadaIstoE.model.PernoiteConsumo;

import java.time.LocalDate;
import java.util.List;

public record PernoiteResponse(
        Long id,
        Client cliente,
        List<AcompanhantePernoiteShortResponse> acompanhantes,
        Quarto quarto,
        LocalDate data_de_entrada,
        LocalDate data_de_saida,
        List<PernoiteConsumo> consumo,
        Valores valores
) {
    public record Client (
        String nome,
        String cpf,
        String telefone
    ){}

    public record Quarto (
        Integer numero
    ){}
    public record Valores(
            Integer quantidade_pessoas,
            Integer quantidade_dias,
            Float valor_diaria,
            Double total_consumo,
            Float total_diarias,
            TipoPagamento tipoPagamento,
            StatusPagamento status_pagamento,
            Double valor_total
    ){}
}