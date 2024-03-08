package com.example.PousadaIstoE.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "a02_county")
public class County {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "county_description")
    private String description;

    @ManyToOne
    @JoinColumn (name = "county_state_id")
    private States state;
}
