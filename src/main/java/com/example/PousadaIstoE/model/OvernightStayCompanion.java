package com.example.PousadaIstoE.model;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.Period;

@Entity
@Table(name = "ip09_overnight_stay_companion")
public class OvernightStayCompanion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ip09_id")
    private Long id;

    @Column(name = "ip09_name")
    private String name;

    @Column(name = "ip09_cpf")
    private String cpf;

    @Column(name = "ip09_birth")
    private LocalDate birth;

    @Column(name = "ip09_age")
    private Integer age;

    @ManyToOne
    @JoinColumn(name = "fkip09ip08_overnight_stay_id")
    private OvernightStay overnightStay;

    public OvernightStayCompanion() {
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

    public OvernightStay getPernoites() {
        return overnightStay;
    }

    public void setPernoites(OvernightStay overnightStay) {
        this.overnightStay = overnightStay;
    }

    public OvernightStayCompanion(String name, String cpf, LocalDate birth, Integer age, OvernightStay overnightStay) {
        this.name = name;
        this.cpf = cpf;
        this.birth = birth;
        this.age = age;
        this.overnightStay = overnightStay;
    }
}
