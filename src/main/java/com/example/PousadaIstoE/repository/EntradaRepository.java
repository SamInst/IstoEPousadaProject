package com.example.PousadaIstoE.repository;

import com.example.PousadaIstoE.model.EntradaConsumo;
import com.example.PousadaIstoE.model.Entradas;
import com.example.PousadaIstoE.model.RegistroDeEntradas;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface EntradaRepository extends JpaRepository <Entradas, Long> {
    List<Entradas> findByApt(Integer apt);
    @Query("select u from RegistroDeEntradas u where u.data = :data")
    List<RegistroDeEntradas> findByData(LocalDate data);

    @Query("delete from EntradaConsumo where entradas.id > 0")
    Entradas deleteEntradaEConsumo();
}