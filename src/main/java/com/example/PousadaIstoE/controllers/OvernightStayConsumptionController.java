package com.example.PousadaIstoE.controllers;

import com.example.PousadaIstoE.model.OverNightStayConsumption;
import com.example.PousadaIstoE.services.OvernightStayConsumptionService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pernoite-consumo")
public class OvernightStayConsumptionController {
    private final OvernightStayConsumptionService overnightStayConsumptionService;

    public OvernightStayConsumptionController(OvernightStayConsumptionService overnightStayConsumptionService) {
        this.overnightStayConsumptionService = overnightStayConsumptionService;
    }

    @GetMapping
    public List<OverNightStayConsumption> buscarTodos(){
        return overnightStayConsumptionService.BuscaTodos();
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public OverNightStayConsumption criaConsumo(OverNightStayConsumption overNightStayConsumption){
        return overnightStayConsumptionService.addConsumo(overNightStayConsumption);
    }
    @DeleteMapping("/{id_consumo}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletaConsumoPernoite(@PathVariable ("id_consumo") Long id_consumo){
         overnightStayConsumptionService.deleteConsumoPernoite(id_consumo);
    }
}
