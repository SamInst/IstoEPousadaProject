package com.example.PousadaIstoE.repository;

import com.example.PousadaIstoE.model.EntradaConsumo;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface EntradaConsumoRepository extends JpaRepository<EntradaConsumo, Long> {
    List<EntradaConsumo> findEntradaConsumoByEntradas_Id(Long id);
}
