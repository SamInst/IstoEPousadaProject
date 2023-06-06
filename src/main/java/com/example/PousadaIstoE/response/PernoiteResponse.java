package com.example.PousadaIstoE.response;

import com.example.PousadaIstoE.model.PernoiteConsumo;

import java.time.LocalDate;
import java.util.List;

public record PernoiteResponse(
        Client cliente,
        Integer apartamento,
        LocalDate data_de_entrada,
        LocalDate data_de_saida,
        List<PernoiteConsumo> consumo,
        Valores valores
) {
    public record Client (
        String nome_cliente,
        String telefone
        ){
    }
    public record Valores(
            Integer quantidade_pessoas,
            Integer quantidade_dias,
            Float valor_diaria,
            Float valor_total,
            TipoPagamento tipoPagamento,
            StatusPagamento status_pagamento
    ){}
}