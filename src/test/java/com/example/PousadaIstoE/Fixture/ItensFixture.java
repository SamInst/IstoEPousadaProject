package com.example.PousadaIstoE.Fixture;

import com.example.PousadaIstoE.model.Itens;

import java.util.ArrayList;
import java.util.List;

public class ItensFixture {
    public static Itens itens(){
        return new Itens("agua com gas", 5F);
    }

    public static Itens itenVazio(){
        return new Itens(8L,"agua com gas", 5F);
    }

    public static List<Itens> itensList(){
        List<Itens> itensList = new ArrayList<>();
        itensList.add(itens());
        return itensList;
    }
}
