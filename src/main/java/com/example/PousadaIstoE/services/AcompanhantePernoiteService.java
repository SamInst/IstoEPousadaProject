package com.example.PousadaIstoE.services;

import com.example.PousadaIstoE.exceptions.EntityNotFound;
import com.example.PousadaIstoE.model.AcompanhantePernoite;
import com.example.PousadaIstoE.repository.AcompanhantePernoiteRepository;
import com.example.PousadaIstoE.repository.PernoitesRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AcompanhantePernoiteService {
    private final AcompanhantePernoiteRepository acompanhantePernoiteRepository;

    private final PernoitesRepository pernoitesRepository;

    public AcompanhantePernoiteService(AcompanhantePernoiteRepository acompanhantePernoiteRepository, PernoitesRepository pernoitesRepository) {
        this.acompanhantePernoiteRepository = acompanhantePernoiteRepository;
        this.pernoitesRepository = pernoitesRepository;
    }

    public List<AcompanhantePernoite> acompanhantePernoiteList(){
        return acompanhantePernoiteRepository.findAll();
    }

    public AcompanhantePernoite findById(Long id){
        return acompanhantePernoiteRepository.findById(id).orElseThrow(
                ()-> new EntityNotFound("Acompanhante não encontrado"));
    }

    public AcompanhantePernoite addAcompanhante(AcompanhantePernoite acompanhante){
        var pernoite = pernoitesRepository.findById(acompanhante.getPernoites().getId()).orElseThrow(
                ()-> new EntityNotFound("Pernoite não encontrado"));
        acompanhante.setPernoites(pernoite);
        return acompanhantePernoiteRepository.save(acompanhante);
    }
    public void removeAcompanhante(Long id){
        acompanhantePernoiteRepository.deleteById(id);
    }
}