package com.example.PousadaIstoE.model;

import com.example.PousadaIstoE.Enums.EntryStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "ip03_entry")
public class Entry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ip03_id")
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "ip03_rooms_id")
    private Rooms rooms;

    @Column(name = "ip03_start_time")
    private LocalDateTime startTime;

    @Column(name = "ip03_end_time")
    private LocalDateTime endTime;

    @Column(name = "ip03_vehicle_plate")
    private String vehiclePlate;

    @Enumerated(EnumType.STRING)
    @Column(name = "ip03_entry_status")
    private EntryStatus entryStatus;

    @Column(name = "ip03_entry_data_register")
    private LocalDate entryDataRegister;

    @Column(name = "ip03_total_entry")
    private Float totalEntry;

    @Column(name = "ip03_obs")
    private String obs;

    @Column(name = "ip03_entry_value")
    private Float entryValue;

    @Column(name = "ip03_consumption_value")
    private Float consumptionValue;

    @Column(name = "ip03_active")
    private Boolean active;

}
