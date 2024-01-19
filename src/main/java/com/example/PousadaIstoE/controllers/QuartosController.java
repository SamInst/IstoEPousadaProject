package com.example.PousadaIstoE.controllers;

import com.example.PousadaIstoE.model.Rooms;
import com.example.PousadaIstoE.services.QuartosService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/quartos")
class QuartosController {
    private final QuartosService quartosService;

    QuartosController(QuartosService quartosService) {
        this.quartosService = quartosService;
    }

    @GetMapping
    public List<Rooms> listAll(){
        return quartosService.quartosList();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Rooms createQuartos(@RequestBody Rooms rooms){
        return quartosService.createQuartos(rooms);
    }

    @PutMapping("/{quartoID}")
    public Rooms AlterarDadoQuarto(@PathVariable ("quartoID") Long quartoId, @RequestBody Rooms rooms){
        return quartosService.updateQuartoData(quartoId, rooms);
    }

    @GetMapping("/find/{id}")
    public Rooms findById(@PathVariable ("id") Long id){
        return quartosService.findQuarto(id);
    }
}
