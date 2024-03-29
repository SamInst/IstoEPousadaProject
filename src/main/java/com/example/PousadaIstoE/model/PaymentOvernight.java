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
@Table(name = "ip11_calculate_payment_type_overnight")
public class PaymentOvernight {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ip11_id", nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "ip11_id_payment_type")
    private PaymentType paymentType;

    @Column(name = "ip11_value")
    private Float value;

    @Column(name = "ip11_payment_status")
    private PaymentStatus paymentStatus;

    @ManyToOne
    @JoinColumn(name = "ip11_id_overnight")
    private OvernightStay overnightStay;

    public PaymentOvernight(PaymentType paymentType, Float value, PaymentStatus paymentStatus, OvernightStay overnightStay) {
        this.paymentType = paymentType;
        this.value = value;
        this.paymentStatus = paymentStatus;
        this.overnightStay = overnightStay;
    }
}
