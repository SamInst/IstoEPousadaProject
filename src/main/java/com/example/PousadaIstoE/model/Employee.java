package com.example.PousadaIstoE.model;
import jakarta.persistence.*;
@Table(name = "tb_employees")
@Entity
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private String name;
    private String cpf;

    public Long getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public String getCpf() {
        return cpf;
    }
    public Employee() {
    }
}