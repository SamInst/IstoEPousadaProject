package com.example.PousadaIstoE.controllers;

import com.example.PousadaIstoE.model.EntradaConsumo;
import com.example.PousadaIstoE.services.EntradaConsumoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/entrada-consumo")
public class EntradaConsumoController {
    private final EntradaConsumoService entradaConsumoService;

    public EntradaConsumoController(EntradaConsumoService entradaConsumoService) {
        this.entradaConsumoService = entradaConsumoService;
    }

    @GetMapping
    public List<EntradaConsumo> buscarTodos(){
        return entradaConsumoService.BuscaTodos();
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public EntradaConsumo criaConsumo(EntradaConsumo entradaConsumo){
        return entradaConsumoService.addConsumo(entradaConsumo);
    }
    @DeleteMapping("/{id_consumo}")
    public ResponseEntity<Object> deletaConsumoPorEntradaId( @PathVariable ("id_consumo") Long id_consumo){
        return  entradaConsumoService.deletaConsumoPorEntradaId(id_consumo);
    }
}
