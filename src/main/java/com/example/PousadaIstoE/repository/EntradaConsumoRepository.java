package com.example.PousadaIstoE.repository;

import com.example.PousadaIstoE.model.EntradaConsumo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface EntradaConsumoRepository extends JpaRepository<EntradaConsumo, Long> {
    List<EntradaConsumo> findEntradaConsumoByEntradas_Id(Long id);

    @Query("select sum(u.itens.valor) from EntradaConsumo u where u.entradas.id = :id")
    List<EntradaConsumo> valorConsumo(Long id);

//    void deleteEntradaConsumoByEntradas_Id(Long id_consumo, Long id_entrada);




}
