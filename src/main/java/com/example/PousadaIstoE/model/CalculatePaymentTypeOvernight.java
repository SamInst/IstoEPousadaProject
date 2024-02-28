package com.example.PousadaIstoE.model;

import jakarta.persistence.*;

@Entity
@Table(name = "a05_calculate_payment_type")
public class CalculatePaymentType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "a05_id", nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "a05_id_payment_type")
    private PaymentType paymentType;

    @Column(name = "a05_value")
    private Float value;

    @ManyToOne
    @JoinColumn(name = "a05_id_overnight")
    private OvernightStay overnightStay;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public PaymentType getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(PaymentType paymentType) {
        this.paymentType = paymentType;
    }

    public Float getValue() {
        return value;
    }

    public void setValue(Float value) {
        this.value = value;
    }

    public OvernightStay getOvernightStay() {
        return overnightStay;
    }

    public void setOvernightStay(OvernightStay overnightStay) {
        this.overnightStay = overnightStay;
    }
}
