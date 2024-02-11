package com.example.PousadaIstoE.controllers;

import com.example.PousadaIstoE.model.OvernightStay;
import com.example.PousadaIstoE.response.OvernightStayResponse;
import com.example.PousadaIstoE.response.OvernightStayShortResponse;
import com.example.PousadaIstoE.services.OvernightStayService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pernoites")
public class OvernightStayController {
    private final OvernightStayService overnightStayService;

    public OvernightStayController(OvernightStayService overnightStayService) { this.overnightStayService = overnightStayService; }
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<OvernightStayShortResponse> findAll() { return overnightStayService.findAll(); }

    @GetMapping("/{pernoiteId}")
    @ResponseStatus(HttpStatus.OK)
    public OvernightStayResponse findbyId(@PathVariable ("pernoiteId") Long id){ return overnightStayService.findById(id); }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OvernightStay createPernoite(OvernightStay overnightStay){ return overnightStayService.createPernoite(overnightStay); }

    @PutMapping("/{pernoiteId}")
    public OvernightStay AlterarDadosPernoite(@PathVariable ("pernoiteId") Long pernoiteId, OvernightStay overnightStay){
        return overnightStayService.updatePernoiteData(pernoiteId, overnightStay);
    }

}
