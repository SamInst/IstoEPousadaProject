package com.example.PousadaIstoE.services;

import com.example.PousadaIstoE.exceptions.EntityNotFound;
import com.example.PousadaIstoE.model.Item;
import com.example.PousadaIstoE.repository.ItemRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ItemService {
    public static final Long ITEM_VAZIO = 8L;
    private final ItemRepository itemRepository;

    public ItemService(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }
    public List<Item> findAll(){
        return itemRepository.findAll();
    }

    public Item findItensById(Long id){
        return itemRepository.findById(id).orElseThrow(()-> new EntityNotFound("Item não encontrado"));
    }

    public Item criaItens(Item item){
        return itemRepository.save(item);
    }

    public Optional<Item> itemConsumoVazio(){
        return itemRepository.findById(ITEM_VAZIO);
    }
}