package com.example.PousadaIstoE.response;

import java.time.LocalDate;

public record PernoiteShortResponse(
    Long id,
    Client cliente,
    Integer apartamento,
    LocalDate data_de_entrada,
    LocalDate data_de_saida,
    Integer quantidadePessoas

){
    public record Client (
        String nome_cliente
    ){}
}
