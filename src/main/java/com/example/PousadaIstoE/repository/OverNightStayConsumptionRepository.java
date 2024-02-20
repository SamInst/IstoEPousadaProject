package com.example.PousadaIstoE.repository;

import com.example.PousadaIstoE.model.OverNightStayConsumption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OverNightStayConsumptionRepository extends JpaRepository<OverNightStayConsumption, Long> {
    @Query(value = """
                SELECT COALESCE(SUM(oc.ip10_total), 0.0)
                FROM ip10_overnight_consumption oc
                WHERE oc.fkip10ip08_overnight_stay_id = :overnight_id
                """, nativeQuery = true)
    Float totalConsumptionByOvernightId(Long overnight_id);

    List<OverNightStayConsumption> findAllByOvernightStay_Id(Long overnight_id);

}