package com.example.PousadaIstoE.repository;

import com.example.PousadaIstoE.model.CalculatePaymentTypeOvernight;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CalculatePaymentTypeRepository extends JpaRepository<CalculatePaymentTypeOvernight, Long> {
    List<CalculatePaymentTypeOvernight> findAllByOvernightStay_Id(Long id);

    @Query(value = """
        select sum(a05.a05_value) from a05_calculate_payment_type a05 where a05_id_overnight = :id
        """, nativeQuery = true)
    Float sumAllValues(Long id);
}