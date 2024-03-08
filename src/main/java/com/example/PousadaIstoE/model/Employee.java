package com.example.PousadaIstoE.model;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
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
}