package com.example.PousadaIstoE.repository;


import com.example.PousadaIstoE.model.EntryConsumption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface EntryConsumptionRepository extends JpaRepository<EntryConsumption, Long> {
    List<EntryConsumption> findEntradaConsumoByEntradas_Id(Long id);
}
