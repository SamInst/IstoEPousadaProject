package com.example.PousadaIstoE.Fixture;

import com.example.PousadaIstoE.model.Quartos;
import com.example.PousadaIstoE.response.StatusDoQuarto;
import java.util.ArrayList;
import java.util.List;

public class QuartosFixture {
    public static Quartos quartoDisponivel(){
        return new Quartos(
                1L,
                1,
                "qarto rgb",
                2,
                StatusDoQuarto.DISPONIVEL
        );
    }
    public static Quartos quartoOcupado(){
        return new Quartos(
                2L,
                1,
                "qarto rgb",
                2,
                StatusDoQuarto.OCUPADO
        );
    }
    public static Quartos quartoReservado(){
        return new Quartos(
                2L,
                1,
                "qarto rgb",
                2,
                StatusDoQuarto.RESERVADO
        );
    }
    public static Quartos quartoSujo(){
        return new Quartos(
                2L,
                1,
                "qarto rgb",
                2,
                StatusDoQuarto.NECESSITA_LIMPEZA
        );
    }
    public static List<Quartos> quartosList (){
        List<Quartos> quartosList = new ArrayList<>();
        quartosList.add(quartoOcupado());
        return quartosList;
    }
}
