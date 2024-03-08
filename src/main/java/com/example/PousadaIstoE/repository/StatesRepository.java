package com.example.PousadaIstoE.repository;

import com.example.PousadaIstoE.model.States;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StatesRepository extends JpaRepository<States, Long> {
}