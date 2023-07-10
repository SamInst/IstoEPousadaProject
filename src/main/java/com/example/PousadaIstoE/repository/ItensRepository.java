package com.example.PousadaIstoE.repository;

import com.example.PousadaIstoE.model.Itens;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ItensRepository extends JpaRepository<Itens, Long> {
    @Query("select m from Itens m where m.id = 8")
    Itens getItenVazio();
}