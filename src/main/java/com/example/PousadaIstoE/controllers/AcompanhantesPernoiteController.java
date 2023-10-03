package com.example.PousadaIstoE.controllers;

import com.example.PousadaIstoE.model.AcompanhantePernoite;
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
    public AcompanhantePernoite findById(@PathVariable("id") Long id){
        return acompanhantePernoiteService.findById(id);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<AcompanhantePernoite> acompanhantesPernoiteList(){
        return acompanhantePernoiteService.acompanhantePernoiteList();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.ACCEPTED)
    public AcompanhantePernoite adicionaAcompanhante(AcompanhantePernoite acompanhantePernoite){
        return acompanhantePernoiteService.addAcompanhante(acompanhantePernoite);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void  deleteById(@PathVariable("id") Long id){
        acompanhantePernoiteService.removeAcompanhante(id);
    }
}
