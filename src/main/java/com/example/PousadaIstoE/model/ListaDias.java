package com.example.PousadaIstoE.model;

import jakarta.persistence.Embedded;

import java.time.LocalDate;
import java.util.List;


public class ListaDias {
List<LocalDate> listaDeDias;

    public List<LocalDate> getListaDeDias() {
        return listaDeDias;
    }

    public void setListaDeDias(List<LocalDate> listaDeDias) {
        this.listaDeDias = listaDeDias;
    }
}
