package com.example.PousadaIstoE.repository;

import com.example.PousadaIstoE.model.OvernightStayReservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface OvernightStayReservationRepository extends JpaRepository<OvernightStayReservation, Long> {
    List<OvernightStayReservation> findAllByStartDate(LocalDate localDate);
}
