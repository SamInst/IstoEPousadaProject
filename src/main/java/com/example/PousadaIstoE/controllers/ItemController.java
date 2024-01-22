package com.example.PousadaIstoE.controllers;

import com.example.PousadaIstoE.model.Item;
import com.example.PousadaIstoE.services.ItemService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/itens")
public class ItemController {
    private final ItemService itemService;

    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }
    @GetMapping
    public List<Item> itensList (){
        return itemService.findAll();
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public Item criaItens(@RequestBody Item item){
        return itemService.criaItens(item);
    }

    @GetMapping("/{itemId}")
    public Item findItemById(@PathVariable ("itemId") Long itemId){
        return itemService.findItensById(itemId);
    }

    @GetMapping("/itemVazio")
    public Optional<Item> itemVazio(){
        return itemService.itemConsumoVazio();
    }
}
