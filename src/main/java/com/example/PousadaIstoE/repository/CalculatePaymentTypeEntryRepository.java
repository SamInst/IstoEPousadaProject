package com.example.PousadaIstoE.repository;

import com.example.PousadaIstoE.model.PaymentEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CalculatePaymentTypeEntryRepository extends JpaRepository<PaymentEntry, Long> {
    List<PaymentEntry> findAllByEntry_Id(Long id);

    @Query(value = """
        select sum(a06_value) from a06_calculate_payment_type_entry a06 where a06_id_entry = :id
        """, nativeQuery = true)
    Float sumAllValues(Long id);
}