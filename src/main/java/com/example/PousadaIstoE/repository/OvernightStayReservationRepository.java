package com.example.PousadaIstoE.repository;

import com.example.PousadaIstoE.model.OvernightStayReservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface OvernightStayReservationRepository extends JpaRepository<OvernightStayReservation, Long> {

    @Query("""
                select u from OvernightStayReservation u where u.isActive and u.startDate = :localDate
                """)
    List<OvernightStayReservation> findAllByStartDateAndActiveIsTrue(LocalDate localDate);
}
