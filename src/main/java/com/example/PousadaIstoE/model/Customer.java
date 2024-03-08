package com.example.PousadaIstoE.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDate;

@Table(name = "ip01_customer")
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ip01_id")
    private Long id;
    @Column(name = "ip01_name")
    private String name;

    @Column(name = "ip01_email")
    private String email;

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

    @Column(name = "ip01_blocked")
    private Boolean isBlocked;

    @Column(name = "ip01_obs")
    private String obs;
}
