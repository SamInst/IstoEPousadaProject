package com.example.PousadaIstoE.response;

import java.time.LocalTime;

public record EntradaSimplesResponse (
        Long id,
        Integer apartamento,
        LocalTime hora_entrada,
        LocalTime hora_saida,
        String placa,
        StatusEntrada statusEntrada
){}