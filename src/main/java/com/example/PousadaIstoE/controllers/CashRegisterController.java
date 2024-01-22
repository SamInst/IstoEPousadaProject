package com.example.PousadaIstoE.controllers;

import com.example.PousadaIstoE.model.CashRegister;
import com.example.PousadaIstoE.response.CashRegisterResponse;
import com.example.PousadaIstoE.services.CashRegisterService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/maps")
public class CashRegisterController {
private final CashRegisterService cashRegisterService;
    public CashRegisterController(CashRegisterService cashRegisterService) {
        this.cashRegisterService = cashRegisterService;
    }

    @GetMapping
    public List<CashRegister> list(){
        return cashRegisterService.listAllMaps();
    }

    @GetMapping("/{userId}")
    public ResponseEntity<CashRegisterResponse> findMapaGeral(@PathVariable("userId") Long id) {
        return cashRegisterService.findMapaGeral(id);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public CashRegister add(@RequestBody CashRegister cashRegister) {
        return cashRegisterService.createMapa(cashRegister);
    }

    @GetMapping("/findByDate")
    public List<CashRegister> findByData(LocalDate date){
        return cashRegisterService.findByData(date);
    }

    @GetMapping("/getTotalMapaGeral")
    public Float totalMapaGeral(){
        return cashRegisterService.totalMapaGeral();
    }
}