package com.example.PousadaIstoE.model;

import jakarta.persistence.*;

@Entity
@Table(name = "ip11_county")
public class County {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ip11_id")
    private Long id;

    @Column(name = "ip11_description")
    private String description;

    @ManyToOne
    @JoinColumn (name = "fkip11ip12_id_state")
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
