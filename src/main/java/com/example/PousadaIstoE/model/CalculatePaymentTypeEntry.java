package com.example.PousadaIstoE.model;

import jakarta.persistence.*;

@Entity
@Table(name = "a06_calculate_payment_type_entry")
public class CalculatePaymentTypeEntry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "a06_id", nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "a06_id_payment_type")
    private PaymentType paymentType;

    @Column(name = "a06_value")
    private Float value;

    @ManyToOne
    @JoinColumn(name = "a06_id_entry")
    private Entry entry;

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

    public Entry getEntry() {
        return entry;
    }

    public void setEntry(Entry entry) {
        this.entry = entry;
    }
}
