package com.example.PousadaIstoE.repository;

import com.example.PousadaIstoE.model.MapaGeral;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface MapaGeralRepository extends JpaRepository<MapaGeral, Long> {
    @Query("select u from MapaGeral u where u.data = :data")
    List<MapaGeral> findByData(LocalDate data);

    @Query("SELECT m.total FROM MapaGeral m WHERE m.id = (SELECT MAX(mg.id) FROM MapaGeral mg)")
    Float findLastTotal();
}
