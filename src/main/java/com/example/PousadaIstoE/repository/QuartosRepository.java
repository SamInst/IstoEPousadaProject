package com.example.PousadaIstoE.repository;

import com.example.PousadaIstoE.model.Quartos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface QuartosRepository extends JpaRepository<Quartos, Long> {

    @Query("select u from Quartos u order by u.numero")
    List<Quartos> findAllOrOrderById();
}
