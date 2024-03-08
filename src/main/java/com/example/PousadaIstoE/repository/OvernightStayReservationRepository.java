package com.example.PousadaIstoE.repository;

import com.example.PousadaIstoE.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface OvernightStayReservationRepository extends JpaRepository<Reservation, Long> {

    Reservation findOvernightStayReservationById(Long overnight_id);

    @Query("""
           select u from Reservation u where u.isActive and u.startDate = :localDate
           """)
    List<Reservation> findAllByStartDateAndActiveIsTrue(LocalDate localDate);

    @Query("""
           select u from Reservation u where u.isActive = false
           """)
    List<Reservation> findAllAndActiveIsFalse();

    @Query(value = """
           select * from ip13_reservations u where u.ip13_is_active = true;
           """, nativeQuery = true)
    List<Reservation> findAllByRoomAndActiveIsTrue(Integer room);
}
