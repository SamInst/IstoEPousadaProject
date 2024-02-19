package com.example.PousadaIstoE.controllers;

import com.example.PousadaIstoE.request.ConsumptionRequest;
import com.example.PousadaIstoE.services.OvernightStayConsumptionService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/overnight/consumption")
public class OvernightStayConsumptionController {
    private final OvernightStayConsumptionService overnightStayConsumptionService;

    public OvernightStayConsumptionController(OvernightStayConsumptionService overnightStayConsumptionService) {
        this.overnightStayConsumptionService = overnightStayConsumptionService;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/create/{overnight_id}")
    public void criaConsumo(@PathVariable Long overnight_id, @RequestBody List<ConsumptionRequest> request){
         overnightStayConsumptionService.addConsumption(overnight_id, request);
    }
    @DeleteMapping("/remove/{id_consumption}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletaConsumoPernoite(@PathVariable Long id_consumption){
         overnightStayConsumptionService.removeConsumption(id_consumption);
    }
}
