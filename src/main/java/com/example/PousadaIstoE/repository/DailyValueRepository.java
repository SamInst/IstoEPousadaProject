package com.example.PousadaIstoE.repository;

import com.example.PousadaIstoE.model.DailyValues;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DailyValueRepository extends JpaRepository<DailyValues, Long> {
}
