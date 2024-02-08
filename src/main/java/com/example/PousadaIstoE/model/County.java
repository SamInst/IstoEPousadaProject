package com.example.PousadaIstoE.model;

import jakarta.persistence.*;

@Entity
@Table(name = "a02_county")
public class County {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "county_id")
    private Long id;

    @Column(name = "county_description")
    private String description;

    @ManyToOne
    @JoinColumn (name = "county_state_id")
    private States state;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public States getState() {
        return state;
    }

    public void setState(States state) {
        this.state = state;
    }
}
