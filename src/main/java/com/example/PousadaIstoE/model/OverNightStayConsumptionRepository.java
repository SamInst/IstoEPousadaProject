package com.example.PousadaIstoE.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OverNightStayConsumptionRepository extends JpaRepository<OverNightStayConsumption, Long> {
    @Query("select sum(m.total) from OverNightStayConsumption m where m.overnightStay.id = :overnight_id")
    Float totalConsumptionByOvernightId(Long overnight_id);

}