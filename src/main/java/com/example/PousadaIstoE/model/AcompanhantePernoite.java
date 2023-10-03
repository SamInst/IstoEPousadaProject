package com.example.PousadaIstoE.model;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.Period;

@Entity
@Table(name = "tb_acompanhante_pernoite")
public class AcompanhantePernoite {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String cpf;
    private LocalDate birth;
    private Integer age;
    @ManyToOne
    private Pernoites pernoites;

    public AcompanhantePernoite() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public LocalDate getBirth() {
        return birth;
    }

    public void setBirth(LocalDate birth) {
        this.birth = birth;
    }

    public Integer getAge() {
    LocalDate today = LocalDate.now();
    var age = Period.between(birth, today);
        return age.getYears();
    }


    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Pernoites getPernoites() {
        return pernoites;
    }

    public void setPernoites(Pernoites pernoites) {
        this.pernoites = pernoites;
    }

    public AcompanhantePernoite(String name, String cpf, LocalDate birth, Integer age, Pernoites pernoites) {
        this.name = name;
        this.cpf = cpf;
        this.birth = birth;
        this.age = age;
        this.pernoites = pernoites;
    }
}
