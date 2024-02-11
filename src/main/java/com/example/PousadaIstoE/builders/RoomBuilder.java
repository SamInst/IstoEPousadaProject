package com.example.PousadaIstoE.builders;

import com.example.PousadaIstoE.Enums.RoomStatus;
import com.example.PousadaIstoE.Enums.RoomType;
import com.example.PousadaIstoE.model.Rooms;

public class RoomBuilder {
    private Long id;
    private Integer number;
    private String description;
    private Integer personCapacity;
    private RoomStatus roomStatus;
    private RoomType roomType;


    public RoomBuilder id(Long id) {
        this.id = id;
        return this;
    }

    public RoomBuilder number(Integer number) {
        this.number = number;
        return this;
    }

    public RoomBuilder description(String description) {
        this.description = description;
        return this;
    }

    public RoomBuilder personCapacity(Integer personCapacity) {
        this.personCapacity = personCapacity;
        return this;
    }

    public RoomBuilder roomStatus(RoomStatus roomStatus) {
        this.roomStatus = roomStatus;
        return this;
    }

    public RoomBuilder roomType(RoomType roomType) {
        this.roomType = roomType;
        return this;
    }

    public Rooms build() {
        return new Rooms(
            id,
            number,
            description,
            personCapacity,
            roomStatus,
            roomType);
    }
}

