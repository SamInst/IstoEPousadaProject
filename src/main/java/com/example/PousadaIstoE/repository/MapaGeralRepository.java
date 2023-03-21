package com.example.PousadaIstoE.repository;

import com.example.PousadaIstoE.model.MapaGeral;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MapaGeralRepository extends JpaRepository<MapaGeral, Long> {

}
