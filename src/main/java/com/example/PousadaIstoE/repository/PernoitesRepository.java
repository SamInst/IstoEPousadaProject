package com.example.PousadaIstoE.repository;

import com.example.PousadaIstoE.model.Pernoites;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface PernoitesRepository extends JpaRepository <Pernoites, Long> {


    List<Pernoites> findByDataEntrada(LocalDate dataEntrada);
    List<Pernoites> findByApartamento_Id(Long apt);
}