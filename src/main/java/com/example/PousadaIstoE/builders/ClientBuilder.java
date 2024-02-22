package com.example.PousadaIstoE.builders;

import com.example.PousadaIstoE.model.*;

import java.time.LocalDate;

public class ClientBuilder {
    private Long id;
    private String name;
    private String email;
    private String cpf;
    private LocalDate birth;
    private String phone;
    private Country country;
    private States state;
    private County county;
    private String address;
    private String job;
    private Employee registeredBy;
    private Boolean isHosted;
    private Boolean isBlocked;
    private String obs;

    public ClientBuilder id(Long id) {
        this.id = id;
        return this;
    }

    public ClientBuilder name(String name) {
        this.name = name;
        return this;
    }

    public ClientBuilder email(String email) {
        this.email = email;
        return this;
    }

    public ClientBuilder cpf(String cpf) {
        this.cpf = cpf;
        return this;
    }

    public ClientBuilder birth(LocalDate birth) {
        this.birth = birth;
        return this;
    }

    public ClientBuilder phone(String phone) {
        this.phone = phone;
        return this;
    }

    public ClientBuilder country(Country country) {
        this.country = country;
        return this;
    }

    public ClientBuilder state(States state) {
        this.state = state;
        return this;
    }

    public ClientBuilder county(County county) {
        this.county = county;
        return this;
    }

    public ClientBuilder address(String address) {
        this.address = address;
        return this;
    }

    public ClientBuilder job(String job) {
        this.job = job;
        return this;
    }

    public ClientBuilder registeredBy(Employee registeredBy) {
        this.registeredBy = registeredBy;
        return this;
    }

    public ClientBuilder isHosted(Boolean isHosted) {
        this.isHosted = isHosted;
        return this;
    }

    public ClientBuilder isBlocked(Boolean isBlocked) {
        this.isBlocked = isBlocked;
        return this;
    }

    public ClientBuilder obs(String obs) {
        this.obs = obs;
        return this;
    }

    public Customer build() {
        return new Customer(
                id,
                name,
                email,
                cpf,
                birth,
                phone,
                country,
                state,
                county,
                address,
                job,
                registeredBy,
                isHosted,
                isBlocked,
                obs);
    }
}
