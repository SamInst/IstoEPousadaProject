package com.example.PousadaIstoE.repository;

import com.example.PousadaIstoE.model.CashRegister;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface CashRegisterRepository extends JpaRepository<CashRegister, Long> {
    @Query("select u from CashRegister u where u.date = :date")
    List<CashRegister> findByData(LocalDate date);

    @Query("SELECT m.total FROM CashRegister m WHERE m.id = (SELECT MAX(mg.id) FROM CashRegister mg)")
    Float findLastTotal();
}
