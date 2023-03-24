package com.example.PousadaIstoE.response;
import java.time.LocalDate;

public record PernoiteResponse(
        Client cliente,
        Integer apartamento,
        LocalDate data_de_entrada,
        LocalDate data_de_saida,
        String consumo,
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
            Float valor_total
    ){}


}
