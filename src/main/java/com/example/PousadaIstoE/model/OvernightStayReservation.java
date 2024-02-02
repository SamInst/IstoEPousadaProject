package com.example.PousadaIstoE.model;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "ip13_reservations")
public class OvernightStayReservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ip13_id_reservations")
    private Long id;

    @Column(name = "ip13_start_date")
    private LocalDate startDate;

    @Column(name = "ip13_end_date")
    private LocalDate endDate;

    @ManyToOne
    @JoinColumn(name = "fkip13ip01_id_client")
    private Client client;
    @OneToMany
    @JoinColumn(name = "fkip13ip12_id_companion")
    private List<OvernightStayCompanion> companion;

    @Column(name = "ip13_room")
    private Integer room;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public List<OvernightStayCompanion> getCompanion() {
        return companion;
    }

    public void setCompanion(List<OvernightStayCompanion> companion) {
        this.companion = companion;
    }

    public Integer getRoom() {
        return room;
    }

    public void setRoom(Integer room) {
        this.room = room;
    }

    public OvernightStayReservation(Long id, LocalDate startDate, LocalDate endDate, Client client, List<OvernightStayCompanion> companion, Integer room) {
        this.id = id;
        this.startDate = startDate;
        this.endDate = endDate;
        this.client = client;
        this.companion = companion;
        this.room = room;
    }

    public OvernightStayReservation() {
    }
}
