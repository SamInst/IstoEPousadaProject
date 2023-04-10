package com.example.PousadaIstoE.controllers;

import com.example.PousadaIstoE.model.Entradas;
import com.example.PousadaIstoE.response.EntradaResponse;
import com.example.PousadaIstoE.services.EntradaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/entradas")
public class EntradaController {
    private final EntradaService entradaService;

    public EntradaController(EntradaService entradaService) {
        this.entradaService = entradaService;
    }


    @GetMapping
    public List<Entradas> findAll(){
        return entradaService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntradaResponse> findById(@PathVariable ("id") Long id){
        return entradaService.findById(id);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public Entradas entradas(Entradas entradas){
        return entradaService.registerEntrada(entradas);
    }

    @PutMapping("/{id}")
    public Entradas atualizarEntrada (@PathVariable ("id") Long entradaID, @RequestBody Entradas entradas){
        return entradaService.updateEntradaData(entradaID, entradas);
    }
}
