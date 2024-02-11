package com.example.PousadaIstoE.request;

import com.example.PousadaIstoE.Enums.RoomType;

public record UpdateRoomRequest(
        Integer number,
        String description,
        Integer person_capacity,
        RoomType room_type
){}
