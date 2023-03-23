package com.example.PousadaIstoE.response;

import java.time.LocalDate;

public record ClienteResponse(
        String nome,
        String CPF,
        String telefone,
        String endereco,
        String profissao,
        String foi_registrado_por
){}
