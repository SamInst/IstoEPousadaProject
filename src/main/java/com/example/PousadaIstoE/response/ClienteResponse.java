package com.example.PousadaIstoE.response;

import java.time.LocalDate;

public record ClienteResponse(
        String nome,
        String CPF,
        String telefone,
        String endereco,
        String profissao,
        LocalDate data_de_entrada,
        LocalDate data_de_saida,
        String foi_registrado_por
){}
