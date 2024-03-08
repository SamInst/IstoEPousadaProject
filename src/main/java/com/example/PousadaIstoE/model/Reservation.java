package com.example.PousadaIstoE.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "ip13_reservations")
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ip13_id_reservations")
    private Long id;

    @Column(name = "ip13_start_date")
    private LocalDate startDate;

    @Column(name = "ip13_end_date")
    private LocalDate endDate;

    @ManyToMany
    @JoinColumn(name = "fkip13ip01_id_customer")
    private List<Customer> customer;

    @Column(name = "ip13_room")
    private Integer room;

    @Column(name = "ip13_is_active")
    private Boolean isActive;

    @Column(name = "ip13_obs")
    private String obs;
}
