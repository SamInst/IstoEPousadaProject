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
    private LocalDate dataEntry;
    private LocalDate dataOut;

    @ManyToOne
    private Employee registeredBy;
    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public Client(Long id, String name, String cpf, String phone, String address, String job, LocalDate dataEntry, LocalDate dataOut, Employee registeredBy) {
        this.id = id;
        this.name = name;
        this.cpf = cpf;
        this.phone = phone;
        this.address = address;
        this.job = job;
        this.dataEntry = dataEntry;
        this.dataOut = dataOut;
        this.registeredBy = registeredBy;
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

    public LocalDate getDataEntry() {
        return dataEntry;
    }

    public LocalDate getDataOut() {
        return dataOut;
    }

    public Employee getRegisteredBy() {
        return registeredBy;
    }
}
