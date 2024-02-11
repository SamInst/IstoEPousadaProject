package com.example.PousadaIstoE.model;

import com.example.PousadaIstoE.Enums.RoomStatus;
import com.example.PousadaIstoE.Enums.RoomType;
import jakarta.persistence.*;

@Entity
@Table(name = "ip04_rooms")
public class Rooms {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ip04_id")
    private Long id;
    @Column(name = "ip04_number")
    private Integer number;

    @Column(name = "ip04_description")
    private String description;

    @Column(name = "ip04_person_capacity")
    private Integer personCapacity;

    @Column(name = "ip04_room_status")
    private RoomStatus roomStatus;

    @Column(name = "ip04_room_type")
    private RoomType roomType;

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

    public RoomType getRoomType() {
        return roomType;
    }

    public void setRoomType(RoomType roomType) {
        this.roomType = roomType;
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

    public Rooms(Long id, Integer number, String description, Integer personCapacity, RoomStatus roomStatus, RoomType roomType) {
        this.id = id;
        this.number = number;
        this.description = description;
        this.personCapacity = personCapacity;
        this.roomStatus = roomStatus;
        this.roomType = roomType;
    }
}
