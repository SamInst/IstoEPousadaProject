package com.example.PousadaIstoE.exceptions.handler;

public record ErroDeValidacao(
         String campo,
         String mensagem
) {
    private static class ErroDeValidacaoBuilder{

    }
}
