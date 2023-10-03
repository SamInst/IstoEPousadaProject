package com.example.PousadaIstoE.controllers;

import com.example.PousadaIstoE.model.PernoiteConsumo;
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
    public List<PernoiteConsumo> buscarTodos(){
        return pernoiteConsumoService.BuscaTodos();
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public PernoiteConsumo criaConsumo(PernoiteConsumo pernoiteConsumo){
        return pernoiteConsumoService.addConsumo(pernoiteConsumo);
    }
    @DeleteMapping("/{id_consumo}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletaConsumoPernoite(@PathVariable ("id_consumo") Long id_consumo){
         pernoiteConsumoService.deleteConsumoPernoite(id_consumo);
    }
}
