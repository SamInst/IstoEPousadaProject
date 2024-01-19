package com.example.PousadaIstoE.controllers;

import com.example.PousadaIstoE.model.OvernightStayCompanion;
import com.example.PousadaIstoE.model.OvernightStay;
import com.example.PousadaIstoE.response.OvernightStayResponse;
import com.example.PousadaIstoE.response.PernoiteShortResponse;
import com.example.PousadaIstoE.services.PernoiteService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pernoites")
public class PernoiteController {
    private final PernoiteService pernoiteService;

    public PernoiteController(PernoiteService pernoiteService) { this.pernoiteService = pernoiteService; }
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<PernoiteShortResponse> findAll() { return pernoiteService.findAll(); }

    @GetMapping("/{pernoiteId}")
    @ResponseStatus(HttpStatus.OK)
    public OvernightStayResponse findbyId(@PathVariable ("pernoiteId") Long id){ return pernoiteService.findById(id); }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OvernightStay createPernoite(OvernightStay overnightStay){ return pernoiteService.createPernoite(overnightStay); }

    @PutMapping("/{pernoiteId}")
    public OvernightStay AlterarDadosPernoite(@PathVariable ("pernoiteId") Long pernoiteId, OvernightStay overnightStay){
        return pernoiteService.updatePernoiteData(pernoiteId, overnightStay);
    }

    @PostMapping("/acompanhante")
    @ResponseStatus(HttpStatus.CREATED)
    public OvernightStayCompanion addAcompanhante(OvernightStayCompanion overnightStayCompanion){
        return pernoiteService.addAcompanhante(overnightStayCompanion);
    }
}
