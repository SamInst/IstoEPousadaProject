package com.example.PousadaIstoE.repository;

import com.example.PousadaIstoE.model.States;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StateRepository extends JpaRepository<States, Long> {
    List<States> findAllByCountry_Id(Long country_id);
}
