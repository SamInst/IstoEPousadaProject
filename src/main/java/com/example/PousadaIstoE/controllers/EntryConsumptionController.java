package com.example.PousadaIstoE.controllers;

import com.example.PousadaIstoE.model.EntryConsumption;
import com.example.PousadaIstoE.services.EntryConsumptionService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/entrada-consumo")
public class EntryConsumptionController {
    private final EntryConsumptionService entryConsumptionService;

    public EntryConsumptionController(EntryConsumptionService entryConsumptionService) {
        this.entryConsumptionService = entryConsumptionService;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<EntryConsumption> buscarTodos(){
        return entryConsumptionService.BuscaTodos();
    }


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EntryConsumption criaConsumo(EntryConsumption entryConsumption){
        return entryConsumptionService.addConsumo(entryConsumption);
    }
    @DeleteMapping("/{id_consumo}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletaConsumoPorEntradaId( @PathVariable ("id_consumo") Long id_consumo){
         entryConsumptionService.deletaConsumoPorEntradaId(id_consumo);
    }

    @GetMapping("/findByEntrada")
    @ResponseStatus(HttpStatus.OK)
    public List<EntryConsumption> findEntradaConsumoByEntrada(Long entrada_id){
        return entryConsumptionService.findEntradaConsumoByEntrada(entrada_id);
    }
}
