package com.example.PousadaIstoE.builders;

import com.example.PousadaIstoE.model.Client;
import com.example.PousadaIstoE.model.OvernightStay;
import com.example.PousadaIstoE.model.OvernightStayCompanion;
import java.time.LocalDate;

public class CompanionBuilder {
    private Long id;
    private String name;
    private String cpf;
    private LocalDate birth;
    private Integer age;
    private Client client;

    public CompanionBuilder id(Long id) {
        this.id = id;
        return this;
    }

    public CompanionBuilder name(String name) {
        this.name = name;
        return this;
    }

    public CompanionBuilder cpf(String cpf) {
        this.cpf = cpf;
        return this;
    }

    public CompanionBuilder birth(LocalDate birth) {
        this.birth = birth;
        return this;
    }

    public CompanionBuilder age(Integer age) {
        this.age = age;
        return this;
    }

    public CompanionBuilder client(Client client) {
        this.client = client;
        return this;
    }

    public OvernightStayCompanion build() {
        OvernightStayCompanion companion = new OvernightStayCompanion();
        companion.setId(id);
        companion.setName(name);
        companion.setCpf(cpf);
        companion.setBirth(birth);
        companion.setAge(age);
        companion.setClient(client);
        return companion;
    }
}
