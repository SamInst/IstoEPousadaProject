package com.example.PousadaIstoE.model;

import com.example.PousadaIstoE.response.RoomStatus;
import jakarta.persistence.*;

@Entity
@Table(name = "ip04_rooms")
public class Rooms {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ip04_id")
    private Long id;
    @Column(name = "ip04_numero")
    private Integer number;

    @Column(name = "ip04_description")
    private String description;

    @Column(name = "ip04_person_capacity")
    private Integer personCapacity;

    @Column(name = "ip04_room_status")
    private RoomStatus roomStatus;

    public void setId(Long id) {this.id = id;}
    public Long getId() {return id;}

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getPersonCapacity() {
        return personCapacity;
    }

    public void setPersonCapacity(Integer personCapacity) {
        this.personCapacity = personCapacity;
    }

    public RoomStatus getRoomStatus() {
        return roomStatus;
    }

    public void setRoomStatus(RoomStatus roomStatus) {
        this.roomStatus = roomStatus;
    }

    public Rooms() {
    }
}
