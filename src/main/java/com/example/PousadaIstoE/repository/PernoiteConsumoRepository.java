package com.example.PousadaIstoE.repository;

import com.example.PousadaIstoE.model.EntradaConsumo;
import com.example.PousadaIstoE.model.PernoiteConsumo;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface PernoiteConsumoRepository extends JpaRepository<PernoiteConsumo, Long> {
    List<PernoiteConsumo> findPernoiteConsumoByPernoites_Id(Long id);
    @Query("SELECT sum(m.total) FROM PernoiteConsumo m WHERE m.pernoites.id = :id")
    Double findConsumoTotal(Long id);
}
