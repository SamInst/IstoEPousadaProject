package com.example.PousadaIstoE.repository;

import com.example.PousadaIstoE.model.RegistroDeEntradas;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface RegistroDeEntradasRepository extends JpaRepository <RegistroDeEntradas, Long> {
    List<RegistroDeEntradas> findByData(LocalDate data);
}