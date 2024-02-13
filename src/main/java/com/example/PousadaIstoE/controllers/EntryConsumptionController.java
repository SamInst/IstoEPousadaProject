package com.example.PousadaIstoE.controllers;

import com.example.PousadaIstoE.request.EntryConsumptionRequest;
import com.example.PousadaIstoE.services.EntryConsumptionService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/entry-consumption")
public class EntryConsumptionController {
    private final EntryConsumptionService entryConsumptionService;

    public EntryConsumptionController(EntryConsumptionService entryConsumptionService) {
        this.entryConsumptionService = entryConsumptionService;
    }


    @PostMapping("/add/{entry_id}")
    @ResponseStatus(HttpStatus.CREATED)
    public void addConsumption(
            @PathVariable Long entry_id,
            @RequestBody EntryConsumptionRequest request) {
         entryConsumptionService.addConsumption(entry_id, request);
    }
    @DeleteMapping("/remove/{consumption_id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeConsumption(@PathVariable Long consumption_id){
         entryConsumptionService.removeConsumption(consumption_id);
    }
}
