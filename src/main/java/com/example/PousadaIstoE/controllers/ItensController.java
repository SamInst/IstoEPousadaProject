package com.example.PousadaIstoE.controllers;

import com.example.PousadaIstoE.model.Itens;
import com.example.PousadaIstoE.services.ItensService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/itens")
public class ItensController {
    private final ItensService itensService;

    public ItensController(ItensService itensService) {
        this.itensService = itensService;
    }
    @GetMapping
    public List<Itens> itensList (){
        return itensService.findAll();
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public Itens criaItens(@RequestBody Itens descricao){
        return itensService.criaItens(descricao);
    }

    @GetMapping("/{itemId}")
    public Itens findItemById(@PathVariable ("itemId") Long itemId){
        return itensService.findItensById(itemId);
    }

    @GetMapping("/itemVazio")
    public Optional<Itens> itemVazio(){
        return itensService.itemConsumoVazio();
    }
}
