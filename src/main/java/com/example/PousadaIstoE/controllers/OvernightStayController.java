package com.example.PousadaIstoE.controllers;

import com.example.PousadaIstoE.model.OvernightStay;
import com.example.PousadaIstoE.response.OvernightStayResponse;
import com.example.PousadaIstoE.response.SimpleOvernightResponse;
import com.example.PousadaIstoE.services.OvernightService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pernoites")
public class OvernightStayController {
    private final OvernightService overnightService;

    public OvernightStayController(OvernightService overnightService) { this.overnightService = overnightService; }
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<SimpleOvernightResponse> findAll() { return overnightService.findAll(); }

    @GetMapping("/{pernoiteId}")
    @ResponseStatus(HttpStatus.OK)
    public OvernightStayResponse findbyId(@PathVariable ("pernoiteId") Long id){ return overnightService.findById(id); }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OvernightStay createPernoite(OvernightStay overnightStay){ return overnightService.createPernoite(overnightStay); }

    @PutMapping("/{pernoiteId}")
    public OvernightStay AlterarDadosPernoite(@PathVariable ("pernoiteId") Long pernoiteId, OvernightStay overnightStay){
        return overnightService.updatePernoiteData(pernoiteId, overnightStay);
    }

}
