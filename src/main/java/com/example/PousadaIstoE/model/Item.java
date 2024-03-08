package com.example.PousadaIstoE.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Setter
@Getter
@NoArgsConstructor
@Entity
@Table(name = "ip07_itens")
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ip07_id")
    private Long id;

    @Column(name = "ip07_description")
    private String description;

    @Column(name = "ip07_value")
    private Float value;
}
