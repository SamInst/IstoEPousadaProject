package com.example.PousadaIstoE.model;

import jakarta.persistence.*;


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

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Float getValue() {
        return value;
    }

    public void setValue(Float value) {
        this.value = value;
    }

    public Item(String description, Float value) {
        this.description = description;
        this.value = value;
    }

    public Item(Long id, String description, Float value) {
        this.id = id;
        this.description = description;
        this.value = value;
    }

    public Item() {
    }
}
