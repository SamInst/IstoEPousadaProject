package com.example.PousadaIstoE.Fixture;

import com.example.PousadaIstoE.model.EntradaConsumo;

import java.util.ArrayList;
import java.util.List;

public class EntradaConsumoFixture {
    public static EntradaConsumo entradaConsumo(){
        return new EntradaConsumo(
                1,
                ItensFixture.itens(),
                EntradasFixture.entradas()
        );
    }
    public static List<EntradaConsumo> entradaConsumoList(){
        List<EntradaConsumo>  entradaConsumosList = new ArrayList<>();
        entradaConsumosList.add(entradaConsumo());
        return entradaConsumosList;
    }
}
