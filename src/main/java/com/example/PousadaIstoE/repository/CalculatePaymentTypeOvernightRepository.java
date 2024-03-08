package com.example.PousadaIstoE.repository;

import com.example.PousadaIstoE.model.PaymentOvernight;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CalculatePaymentTypeOvernightRepository extends JpaRepository<PaymentOvernight, Long> {
    List<PaymentOvernight> findAllByOvernightStay_Id(Long overnight_id);
    @Query(value = """
        select sum(a05.a05_value) from a05_calculate_payment_type a05 where a05_id_overnight = :id
        """, nativeQuery = true)
    Float sumAllValues(Long id);
}