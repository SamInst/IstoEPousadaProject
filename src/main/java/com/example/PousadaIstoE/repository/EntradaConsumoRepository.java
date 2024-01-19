package com.example.PousadaIstoE.repository;


import com.example.PousadaIstoE.model.EntradaConsumption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface EntradaConsumoRepository extends JpaRepository<EntradaConsumption, Long> {
    List<EntradaConsumption> findEntradaConsumoByEntradas_Id(Long id);
}
