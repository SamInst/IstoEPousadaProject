package com.example.PousadaIstoE.repository;

import com.example.PousadaIstoE.model.AcompanhantePernoite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AcompanhantePernoiteRepository extends JpaRepository<AcompanhantePernoite, Long> {
    List<AcompanhantePernoite> findAllByPernoites_Id(Long id);
}
