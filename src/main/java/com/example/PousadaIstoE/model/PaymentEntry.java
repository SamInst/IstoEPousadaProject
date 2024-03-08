package com.example.PousadaIstoE.model;

import com.example.PousadaIstoE.Enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "ip12_calculate_payment_type_entry")
public class PaymentEntry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ip12_id", nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "ip12_id_payment_type")
    private PaymentType paymentType;

    @Column(name = "ip12_value")
    private Float value;

    @ManyToOne
    @JoinColumn(name = "ip12_id_entry")
    private Entry entry;

    @Column(name = "ip12_payment_status")
    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;
}
