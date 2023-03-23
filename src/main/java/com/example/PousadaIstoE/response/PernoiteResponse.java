package com.example.PousadaIstoE.response;
import java.time.LocalDate;

public record PernoiteResponse(
        Integer apartamento,
        LocalDate data_de_entrada,
        LocalDate data_de_saida,
        String consumo,
        Client cliente,
        Employee foi_registrado_por
) {
    public record Client (
        String nome_cliente,
        String telefone
        ){
    }
    public record Employee (
            String nome
    ){}
}
