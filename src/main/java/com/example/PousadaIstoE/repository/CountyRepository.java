package com.example.PousadaIstoE.repository;

import com.example.PousadaIstoE.model.County;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CountyRepository extends JpaRepository<County,Long> {
    List<County> findCountiesByState_Id(Long state_id);
}
