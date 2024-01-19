package com.example.PousadaIstoE.controllers;

import com.example.PousadaIstoE.model.OvernightStayCompanion;
import com.example.PousadaIstoE.services.AcompanhantePernoiteService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/acompanhantes")
public class AcompanhantesPernoiteController {
    private final AcompanhantePernoiteService acompanhantePernoiteService;

    public AcompanhantesPernoiteController(AcompanhantePernoiteService acompanhantePernoiteService) {
        this.acompanhantePernoiteService = acompanhantePernoiteService;
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public OvernightStayCompanion findById(@PathVariable("id") Long id){
        return acompanhantePernoiteService.findById(id);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<OvernightStayCompanion> acompanhantesPernoiteList(){
        return acompanhantePernoiteService.acompanhantePernoiteList();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.ACCEPTED)
    public OvernightStayCompanion adicionaAcompanhante(OvernightStayCompanion overnightStayCompanion){
        return acompanhantePernoiteService.addAcompanhante(overnightStayCompanion);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void  deleteById(@PathVariable("id") Long id){
        acompanhantePernoiteService.removeAcompanhante(id);
    }
}
