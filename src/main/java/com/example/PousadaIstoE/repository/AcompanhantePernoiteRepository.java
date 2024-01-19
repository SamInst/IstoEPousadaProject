package com.example.PousadaIstoE.repository;

import com.example.PousadaIstoE.model.OvernightStayCompanion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AcompanhantePernoiteRepository extends JpaRepository<OvernightStayCompanion, Long> {
    List<OvernightStayCompanion> findAllByPernoites_Id(Long id);
}
