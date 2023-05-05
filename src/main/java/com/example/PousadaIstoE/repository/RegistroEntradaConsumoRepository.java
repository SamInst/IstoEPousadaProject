package com.example.PousadaIstoE.repository;

import com.example.PousadaIstoE.model.RegistroConsumoEntrada;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RegistroEntradaConsumoRepository extends JpaRepository<RegistroConsumoEntrada, Long> {
}
