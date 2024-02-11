package com.example.PousadaIstoE.controllers;

import com.example.PousadaIstoE.Enums.RoomStatus;
import com.example.PousadaIstoE.model.Rooms;
import com.example.PousadaIstoE.request.RoomRequest;
import com.example.PousadaIstoE.request.UpdateRoomRequest;
import com.example.PousadaIstoE.services.RoomService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rooms")
class RoomController {
    private final RoomService roomService;

    RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Rooms> listAll(){
        return roomService.findAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createQuartos(@RequestBody RoomRequest rooms){
        roomService.createRoom(rooms);
    }

    @PutMapping("/{room_id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void updateRoom(@PathVariable Long room_id, @RequestBody UpdateRoomRequest request){
        roomService.updateRoom(room_id, request);
    }

    @GetMapping("/find_by_id/{room_id}")
    @ResponseStatus(HttpStatus.OK)
    public Rooms findById(@PathVariable Long room_id){
        return roomService.findRoomById(room_id);
    }

    @PutMapping("/update_room_status")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void updateRoomStatus(Long room_id, RoomStatus status){
        roomService.updateRoomStatus(room_id, status);
    }

    @GetMapping("/find_by_status/{status}")
    @ResponseStatus(HttpStatus.OK)
    public List<Rooms> findRoomByStatus(@PathVariable RoomStatus status){
       return roomService.findRoomByStatus(status);
    }
}
