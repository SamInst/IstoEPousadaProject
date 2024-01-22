package com.example.PousadaIstoE.controllers;

import com.example.PousadaIstoE.model.OvernightStayCompanion;
import com.example.PousadaIstoE.services.OvernightStayCompanionService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/acompanhantes")
public class OvernightStayCompanionController {
    private final OvernightStayCompanionService overnightStayCompanionService;

    public OvernightStayCompanionController(OvernightStayCompanionService overnightStayCompanionService) {
        this.overnightStayCompanionService = overnightStayCompanionService;
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public OvernightStayCompanion findById(@PathVariable("id") Long id){
        return overnightStayCompanionService.findById(id);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<OvernightStayCompanion> acompanhantesPernoiteList(){
        return overnightStayCompanionService.acompanhantePernoiteList();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.ACCEPTED)
    public OvernightStayCompanion adicionaAcompanhante(OvernightStayCompanion overnightStayCompanion){
        return overnightStayCompanionService.addAcompanhante(overnightStayCompanion);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void  deleteById(@PathVariable("id") Long id){
        overnightStayCompanionService.removeAcompanhante(id);
    }
}
