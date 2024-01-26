package com.example.PousadaIstoE.repository;

import com.example.PousadaIstoE.model.OverNightStayConsumption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface OvernightStayComsuptionRepository extends JpaRepository<OverNightStayConsumption, Long> {
    List<OverNightStayConsumption> findOverNightStayConsumptionByOvernightStay_Id(Long id);
    @Query("SELECT sum(m.total) FROM OverNightStayConsumption m WHERE m.overnightStay.id = :id")
    Double findTotalConsumption(Long id);
}
