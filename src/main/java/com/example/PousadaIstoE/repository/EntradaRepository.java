package com.example.PousadaIstoE.repository;

import com.example.PousadaIstoE.model.Entradas;
import com.example.PousadaIstoE.response.EntradaStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface EntradaRepository extends JpaRepository<Entradas, Long> {
    List<Entradas> findEntradasByStatusEntrada(EntradaStatus entradaStatus);
    List<Entradas> findEntradasByDataRegistroEntrada(LocalDate localDate);
    @Query("select sum(m.total) from EntradaConsumption m where m.entradas.id = :id_entrada")
    Double totalConsumo(Long id_entrada);
    @Query("select u from Entradas u where u.dataRegistroEntrada = :data")
    List<Entradas> findEntradasByData(LocalDate data);
}