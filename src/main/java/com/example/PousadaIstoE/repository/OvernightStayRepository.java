package com.example.PousadaIstoE.repository;

import com.example.PousadaIstoE.model.OvernightStay;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface OvernightStayRepository extends JpaRepository <OvernightStay, Long> {
    List<OvernightStay> findByStartDate(LocalDate dataEntrada);
    List<OvernightStay> findByEndDate(LocalDate dataEntrada);
    List<OvernightStay> findByRoom_Id(Long apt);


}