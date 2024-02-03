package com.example.PousadaIstoE.services;

import com.example.PousadaIstoE.exceptions.EntityNotFound;
import com.example.PousadaIstoE.model.OvernightStayCompanion;
import com.example.PousadaIstoE.repository.OvernightStayCompanionRepository;
import com.example.PousadaIstoE.repository.OvernightStayRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OvernightStayCompanionService {
    private final OvernightStayCompanionRepository overnightStayCompanionRepository;

    private final OvernightStayRepository overnightStayRepository;

    public OvernightStayCompanionService(OvernightStayCompanionRepository overnightStayCompanionRepository, OvernightStayRepository overnightStayRepository) {
        this.overnightStayCompanionRepository = overnightStayCompanionRepository;
        this.overnightStayRepository = overnightStayRepository;
    }

    public List<OvernightStayCompanion> acompanhantePernoiteList(){
        return overnightStayCompanionRepository.findAll();
    }

    public OvernightStayCompanion findById(Long id){
        return overnightStayCompanionRepository.findById(id).orElseThrow(
                ()-> new EntityNotFound("Acompanhante não encontrado"));
    }

//    public OvernightStayCompanion addAcompanhante(OvernightStayCompanion acompanhante){
//        var pernoite = overnightStayRepository.findById(acompanhante.getPernoites().getId()).orElseThrow(
//                ()-> new EntityNotFound("Pernoite não encontrado"));
//        acompanhante.setPernoites(pernoite);
//        return overnightStayCompanionRepository.save(acompanhante);
//    }
    public void removeAcompanhante(Long id){
        overnightStayCompanionRepository.deleteById(id);
    }
}
