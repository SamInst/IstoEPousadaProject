package com.example.PousadaIstoE.controllers;

import com.example.PousadaIstoE.model.OverNightStayConsumption;
import com.example.PousadaIstoE.services.PernoiteConsumoService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pernoite-consumo")
public class PernoiteConsumoController {
    private final PernoiteConsumoService pernoiteConsumoService;

    public PernoiteConsumoController(PernoiteConsumoService pernoiteConsumoService) {
        this.pernoiteConsumoService = pernoiteConsumoService;
    }

    @GetMapping
    public List<OverNightStayConsumption> buscarTodos(){
        return pernoiteConsumoService.BuscaTodos();
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public OverNightStayConsumption criaConsumo(OverNightStayConsumption overNightStayConsumption){
        return pernoiteConsumoService.addConsumo(overNightStayConsumption);
    }
    @DeleteMapping("/{id_consumo}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletaConsumoPernoite(@PathVariable ("id_consumo") Long id_consumo){
         pernoiteConsumoService.deleteConsumoPernoite(id_consumo);
    }
}
