package com.example.PousadaIstoE.repository;

import com.example.PousadaIstoE.model.Client;
import com.example.PousadaIstoE.model.Entradas;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EntradaRepository extends JpaRepository <Entradas, Long> {
    List<Entradas> findByApt(Integer apt);
}