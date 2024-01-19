package com.example.PousadaIstoE.model;

import jakarta.persistence.*;

@Table(name = "ip01_clients")
@Entity
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ip01_id")
    private Long id;
    @Column(name = "ip01_name")
    private String name;

    @Column(name = "ip01_cpf")
    private String cpf;
    @Column(name = "ip01_phone")
    private String phone;

    @Column(name = "ip01_address")
    private String address;
    @Column(name = "ip01_job")
    private String  job;

    @ManyToOne
    @JoinColumn(name = "fkip01ip02_id_employee")
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

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public void setRegisteredBy(Employee registeredBy) {
        this.registeredBy = registeredBy;
    }


    public Client(String name, String cpf, String phone, String address, String job, Employee registeredBy) {
        this.name = name;
        this.cpf = cpf;
        this.phone = phone;
        this.address = address;
        this.job = job;
        this.registeredBy = registeredBy;
    }
}
