package com.example.PousadaIstoE.model;

import com.example.PousadaIstoE.Enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@Entity
@Table(name = "ip14_calculate_payment_type_reservation")
public class PaymentReservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ip14_id", nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "ip14_id_payment_type")
    private PaymentType paymentType;

    @Column(name = "ip14_value")
    private Float value;

    @Column(name = "ip14_payment_status")
    private PaymentStatus paymentStatus;

    @ManyToOne
    @JoinColumn(name = "ip14_id_reservation")
    private Reservation reservation;

    public PaymentReservation(PaymentType paymentType, Float value, PaymentStatus paymentStatus, Reservation reservation) {
        this.paymentType = paymentType;
        this.value = value;
        this.paymentStatus = paymentStatus;
        this.reservation = reservation;
    }
}
