package com.example.PousadaIstoE.controllers;

import com.example.PousadaIstoE.model.CashRegister;
import com.example.PousadaIstoE.response.CashRegisterResponse;
import com.example.PousadaIstoE.services.MapaGeralService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/maps")
public class MapaGeralController {
private final MapaGeralService mapaGeralService;
    public MapaGeralController(MapaGeralService mapaGeralService) {
        this.mapaGeralService = mapaGeralService;
    }

    @GetMapping
    public List<CashRegister> list(){
        return mapaGeralService.listAllMaps();
    }

    @GetMapping("/{userId}")
    public ResponseEntity<CashRegisterResponse> findMapaGeral(@PathVariable("userId") Long id) {
        return mapaGeralService.findMapaGeral(id);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public CashRegister add(@RequestBody CashRegister cashRegister) {
        return mapaGeralService.createMapa(cashRegister);
    }

    @GetMapping("/findByDate")
    public List<CashRegister> findByData(LocalDate date){
        return mapaGeralService.findByData(date);
    }

    @GetMapping("/getTotalMapaGeral")
    public Float totalMapaGeral(){
        return mapaGeralService.totalMapaGeral();
    }
}