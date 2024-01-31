package com.example.PousadaIstoE.repository;

import com.example.PousadaIstoE.model.CashRegister;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface CashRegisterRepository extends JpaRepository<CashRegister, Long> {
    @Query(value = "select * from ip05_cash_register cr where cr.ip05_data = :date", nativeQuery = true)
    List<CashRegister> findByDate(@Param("date") LocalDate date);


    @Query(value = """
            select cr.ip05_total
            from ip05_cash_register cr 
            order by cr.ip05_id desc limit 1
            """, nativeQuery = true)
    Float findLastTotal();
}
