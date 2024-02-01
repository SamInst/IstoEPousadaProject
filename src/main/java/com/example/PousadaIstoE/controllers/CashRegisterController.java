package com.example.PousadaIstoE.controllers;

import com.example.PousadaIstoE.model.CashRegister;
import com.example.PousadaIstoE.request.CashRegisterRequest;
import com.example.PousadaIstoE.response.CashRegisterResponse;
import com.example.PousadaIstoE.services.CashRegisterService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/cashRegister")
public class CashRegisterController {
    private final CashRegisterService cashRegisterService;

    public CashRegisterController(CashRegisterService cashRegisterService) {
        this.cashRegisterService = cashRegisterService;
    }

    @GetMapping("/all")
    public Page<CashRegisterResponse> listAllMaps(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return cashRegisterService.listAllMaps(pageable);
    }

    @PostMapping("/create")
    public void createMapa(@RequestBody CashRegisterRequest request) {
        cashRegisterService.createMapa(request);
    }

    @GetMapping("/find-by-date")
    public List<CashRegister> findByDate(LocalDate date){
        return cashRegisterService.findByDate(date);
    }


}