package com.example.PousadaIstoE.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "a04_payment_type")
public class PaymentType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "a04_id")
    private Long id;

    @Column(name = "a04_description")
    private String description;

    public PaymentType() {
    }

    public PaymentType(Long id, String description) {
        this.id = id;
        this.description = description;
    }
}
