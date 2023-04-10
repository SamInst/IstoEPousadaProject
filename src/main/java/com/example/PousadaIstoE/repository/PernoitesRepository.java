package com.example.PousadaIstoE.repository;

import com.example.PousadaIstoE.model.Pernoites;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PernoitesRepository extends JpaRepository <Pernoites, Long> {


    List<Pernoites> findByApt (Integer apt);
}