package com.example.PousadaIstoE.repository;

import com.example.PousadaIstoE.model.CalculatePaymentType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CalculatePaymentTypeRepository extends JpaRepository<CalculatePaymentType, Long> {
    List<CalculatePaymentType> findAllByOvernightStay_Id(Long id);
}