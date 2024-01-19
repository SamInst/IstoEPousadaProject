package com.example.PousadaIstoE.model;
import jakarta.persistence.*;

@Entity
@Table(name = "ip02_employees")
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ip02_id")
    private Long id;
    @Column(name = "ip02_name")
    private String name;

    @Column(name = "ip02_cpf")
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

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }
}