package com.example.PousadaIstoE.repository;

import com.example.PousadaIstoE.model.Rooms;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuartosRepository extends JpaRepository<Rooms, Long> {
}
