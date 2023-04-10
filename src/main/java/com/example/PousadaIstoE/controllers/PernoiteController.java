package com.example.PousadaIstoE.controllers;

import com.example.PousadaIstoE.model.Pernoites;
import com.example.PousadaIstoE.response.PernoiteResponse;
import com.example.PousadaIstoE.services.PernoiteService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pernoites")
public class PernoiteController {
    private final PernoiteService pernoiteService;

    public PernoiteController(PernoiteService pernoiteService) {
        this.pernoiteService = pernoiteService;
    }
    @GetMapping
    public List<Pernoites> findAll() { return pernoiteService.findAll(); }

    @GetMapping("/{pernoiteId}")
    public ResponseEntity<PernoiteResponse> findbyId(@PathVariable ("pernoiteId") Long id){
        return pernoiteService.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Pernoites createPernoite(Pernoites pernoites){
        return pernoiteService.createPernoite(pernoites);
    }
    @PutMapping("/{pernoiteId}")
    public Pernoites AlterarDadosPernoite(@PathVariable ("pernoiteId") Long pernoiteId, @RequestBody Pernoites pernoites){
        return pernoiteService.updatePernoiteData(pernoiteId, pernoites);
    }
}
