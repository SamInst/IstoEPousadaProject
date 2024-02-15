package com.example.PousadaIstoE.model;

import jakarta.persistence.*;

import java.time.LocalDate;

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

    @Column(name = "ip01_birth")
    private LocalDate birth;
    @Column(name = "ip01_phone")
    private String phone;

    @ManyToOne
    @JoinColumn(name = "fkip01_id_country")
    private Country country;

    @ManyToOne
    @JoinColumn(name = "fkip01_id_state")
    private States state;

    @ManyToOne
    @JoinColumn(name = "fkip01_id_county")
    private County county;

    @Column(name = "ip01_address")
    private String address;
    @Column(name = "ip01_job")
    private String  job;

    @ManyToOne
    @JoinColumn(name = "fkip01ip02_id_employee")
    private Employee registeredBy;

    @Column(name = "ip01_hosted")
    private Boolean isHosted;

    public Long getId() {
        return id;
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public States getState() {
        return state;
    }

    public void setState(States state) {
        this.state = state;
    }

    public County getCounty() {
        return county;
    }

    public void setCounty(County county) {
        this.county = county;
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

    public boolean isHosted() {
        return isHosted;
    }

    public void setHosted(boolean hosted) {
        this.isHosted = hosted;
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

    public LocalDate getBirth() {
        return birth;
    }

    public void setBirth(LocalDate birth) {
        this.birth = birth;
    }

    public Client(String name, String cpf, String phone, String address, String job, Employee registeredBy) {
        this.name = name;
        this.cpf = cpf;
        this.phone = phone;
        this.address = address;
        this.job = job;
        this.registeredBy = registeredBy;
    }

    public Boolean getHosted() {
        return isHosted;
    }

    public void setHosted(Boolean hosted) {
        isHosted = hosted;
    }
}
