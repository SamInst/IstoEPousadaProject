package com.example.PousadaIstoE.repository;

import com.example.PousadaIstoE.model.PaymentReservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CalculatePaymentTypeReservationRepository extends JpaRepository<PaymentReservation, Long> {
    List<PaymentReservation> findAllByReservation_Id(Long id);

}