package com.example.PousadaIstoE.services;

import com.example.PousadaIstoE.Enums.RoomStatus;
import com.example.PousadaIstoE.builders.RoomBuilder;
import com.example.PousadaIstoE.model.Rooms;
import com.example.PousadaIstoE.repository.RoomRepository;
import com.example.PousadaIstoE.request.RoomRequest;
import com.example.PousadaIstoE.request.UpdateRoomRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoomService {
    private final RoomRepository roomRepository;

    private final Finder find;

    public RoomService(RoomRepository roomRepository, Finder find) {
        this.roomRepository = roomRepository;
        this.find = find;
    }

    public List<Rooms> findAll() {
        return roomRepository.findAll();
    }

    public Rooms findRoomById(Long room_id) {
        return find.roomById(room_id);
    }

    public void createRoom(RoomRequest request){
        Rooms room = new RoomBuilder()
                .number(request.number())
                .description(replace(request.description()))
                .personCapacity(request.person_capacity())
                .roomStatus(RoomStatus.AVAIABLE)
                .roomType(request.room_type())
                .build();
        roomRepository.save(room);
    }

    public void updateRoom(Long room_id, UpdateRoomRequest request){
        var room = find.roomById(room_id);
        Rooms updatedRoom = new RoomBuilder()
                .id(room.getId())
                .number(request.number())
                .description(replace(request.description()))
                .personCapacity(request.person_capacity())
                .roomStatus(room.getRoomStatus())
                .roomType(request.room_type())
                .build();
        roomRepository.save(updatedRoom);
    }

    public void updateRoomStatus(Long room_id, RoomStatus status){
        var room = find.roomById(room_id);
        room.setRoomStatus(status);
        roomRepository.save(room);
    }

    public List<Rooms> findRoomByStatus(RoomStatus status){
        return roomRepository.findAllByRoomStatus(status);
    }

    public String replace(String string){return find.replace(string); }
}
