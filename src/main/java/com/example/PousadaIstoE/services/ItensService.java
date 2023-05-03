package com.example.PousadaIstoE.services;

import com.example.PousadaIstoE.model.Itens;
import com.example.PousadaIstoE.repository.ItensRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ItensService {
    private final ItensRepository itensRepository;

    public ItensService(ItensRepository itensRepository) {
        this.itensRepository = itensRepository;
    }
    public List<Itens> findAll(){
        return itensRepository.findAll();
    }

    public Itens criaItens(Itens descricao){
        return itensRepository.save(descricao);
    }
}