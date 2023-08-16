package com.example.PousadaIstoE.repository;

import com.example.PousadaIstoE.model.AcompanhantesPernoite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AcompanhantePernoiteRepository extends JpaRepository<AcompanhantesPernoite, Long> {
}
