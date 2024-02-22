package com.example.PousadaIstoE.repository;

import com.example.PousadaIstoE.model.DailyValues;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DailyValuesRepository extends JpaRepository<DailyValues, Long> {
}