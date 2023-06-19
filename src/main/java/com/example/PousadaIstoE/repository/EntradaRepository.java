package com.example.PousadaIstoE.repository;

import com.example.PousadaIstoE.model.Entradas;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface EntradaRepository extends JpaRepository <Entradas, Long> {
    List<Entradas> findByQuartos_Numero(Integer quartos_numero);
}