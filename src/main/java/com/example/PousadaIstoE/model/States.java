package com.example.PousadaIstoE.model;

import jakarta.persistence.*;

@Entity
@Table(name = "ip12_states")
public class States {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;


    private String description;

    public Long getId() {
        return id;
    }

    @ManyToOne
    @JoinColumn (name = "fkip11ip14_id_country")
    private Country country;

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }
}
