package com.example.PousadaIstoE.repository;

import com.example.PousadaIstoE.model.Client;
import com.example.PousadaIstoE.model.Entradas;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EntradaRepository extends JpaRepository <Entradas, Long> {
}