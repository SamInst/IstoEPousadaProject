package com.example.PousadaIstoE.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OverNightStayConsumptionRepository extends JpaRepository<OverNightStayConsumption, Long> {
    @Query(value = """
            select count(ip10.ip10_total)
            from ip10_overnight_consumption ip10
            where ip10.fkip10ip08_overnight_stay_id = :overnight_id
            """, nativeQuery = true)
    Float totalConsumption(Long overnight_id);

    List<OverNightStayConsumption> findAllByOvernightStay_Id(Long overnight_id);
}