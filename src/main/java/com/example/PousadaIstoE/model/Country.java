package com.example.PousadaIstoE.model;

import jakarta.persistence.*;

@Entity(name = "ip14_country")
public class Country {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ip14_id", nullable = false)
    private Long id;

    @Column("ip14_description")
    private String description;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
