package com.example.PousadaIstoE.model;

import jakarta.persistence.*;

import java.time.LocalDate;
@Table(name = "tb_clients")
@Entity
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private String name;
    private String cpf;
    private String phone;
    private String address;
    private String  job;

    @ManyToOne
    private Employee registeredBy;


    public Long getId() {
        return id;
    }
    public Client (){}
    public String getName() {
        return name;
    }
    public String getCpf() {
        return cpf;
    }
    public String getPhone() {
        return phone;
    }
    public String getAddress() {
        return address;
    }
    public String getJob() {
        return job;
    }
    public Employee getRegisteredBy() {return registeredBy; }
}
