package com.example.PousadaIstoE.repository;

import com.example.PousadaIstoE.model.OvernightStayReservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OvernightStayReservationRepository extends JpaRepository<OvernightStayReservation, Long> {
}
