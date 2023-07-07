package com.example.PousadaIstoE.services;

import com.example.PousadaIstoE.exceptions.EntityNotFound;
import com.example.PousadaIstoE.model.Itens;
import com.example.PousadaIstoE.repository.ItensRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ItensService {
    public static final Long ITEM_VAZIO = 8L;
    private final ItensRepository itensRepository;

    public ItensService(ItensRepository itensRepository) {
        this.itensRepository = itensRepository;
    }
    public List<Itens> findAll(){
        return itensRepository.findAll();
    }

    public Itens findItensById(Long id){
        return itensRepository.findById(id).orElseThrow(()-> new EntityNotFound("Item não encontrado"));
    }

    public Itens criaItens(Itens itens){
        return itensRepository.save(itens);
    }

    public Optional<Itens> itemConsumoVazio(){
        return itensRepository.findById(ITEM_VAZIO);
    }
}