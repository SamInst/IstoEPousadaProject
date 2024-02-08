package com.example.PousadaIstoE.builders;

import com.example.PousadaIstoE.model.Client;
import com.example.PousadaIstoE.model.Employee;

import java.time.LocalDate;

public class ClientBuilder {
    private Long id;
    private String name;
    private String cpf;
    private LocalDate birth;
    private String phone;
    private String address;
    private String job;
    private Employee registeredBy;
    private Boolean hosted;

    public ClientBuilder id(Long id) {
        this.id = id;
        return this;
    }

    public ClientBuilder name(String name) {
        this.name = name;
        return this;
    }

    public ClientBuilder cpf(String cpf) {
        this.cpf = cpf.replaceAll(".-","");
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

    public ClientBuilder isHosted(Boolean hosted) {
        this.hosted = hosted;
        return this;
    }

    public Client build() {
        Client client = new Client();
        client.setId(this.id);
        client.setName(this.name);
        client.setCpf(this.cpf);
        client.setBirth(this.birth);
        client.setPhone(this.phone);
        client.setAddress(this.address);
        client.setJob(this.job);
        client.setRegisteredBy(this.registeredBy);
        client.setHosted(this.hosted);
        return client;
    }
}
