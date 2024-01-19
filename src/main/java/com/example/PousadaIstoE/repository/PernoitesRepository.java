package com.example.PousadaIstoE.repository;

import com.example.PousadaIstoE.model.OvernightStay;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface PernoitesRepository extends JpaRepository <OvernightStay, Long> {
    List<OvernightStay> findByDataEntrada(LocalDate dataEntrada);
    List<OvernightStay> findByDataSaida(LocalDate dataEntrada);
    List<OvernightStay> findByApartamento_Id(Long apt);


}