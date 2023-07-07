package com.example.PousadaIstoE.exceptions.handler;

import java.util.List;

public record ErrorMessageValidacao(
        String projeto,
        String apiPath,
        String error,
        String descricao,
        String exception,
        List<ErroDeValidacao> erroDeValidacao
) {
}
