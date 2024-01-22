package com.example.PousadaIstoE.controllers;

import com.example.PousadaIstoE.model.Rooms;
import com.example.PousadaIstoE.services.RoomService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/quartos")
class RoomController {
    private final RoomService roomService;

    RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    @GetMapping
    public List<Rooms> listAll(){
        return roomService.quartosList();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Rooms createQuartos(@RequestBody Rooms rooms){
        return roomService.createQuartos(rooms);
    }

    @PutMapping("/{quartoID}")
    public Rooms AlterarDadoQuarto(@PathVariable ("quartoID") Long quartoId, @RequestBody Rooms rooms){
        return roomService.updateQuartoData(quartoId, rooms);
    }

    @GetMapping("/find/{id}")
    public Rooms findById(@PathVariable ("id") Long id){
        return roomService.findQuarto(id);
    }
}
