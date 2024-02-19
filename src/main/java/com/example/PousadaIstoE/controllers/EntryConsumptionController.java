package com.example.PousadaIstoE.controllers;

import com.example.PousadaIstoE.request.ConsumptionRequest;
import com.example.PousadaIstoE.services.EntryConsumptionService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/entry/consumption")
public class EntryConsumptionController {
    private final EntryConsumptionService entryConsumptionService;

    public EntryConsumptionController(EntryConsumptionService entryConsumptionService) {
        this.entryConsumptionService = entryConsumptionService;
    }


    @PostMapping("/create/{entry_id}")
    @ResponseStatus(HttpStatus.CREATED)
    public void addConsumption(
            @PathVariable Long entry_id,
            @RequestBody List<ConsumptionRequest> request) {
         entryConsumptionService.addConsumptionToEntry(entry_id, request);
    }
    @DeleteMapping("/remove/{consumption_id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeConsumption(@PathVariable Long consumption_id){
         entryConsumptionService.removeConsumption(consumption_id);
    }
}
