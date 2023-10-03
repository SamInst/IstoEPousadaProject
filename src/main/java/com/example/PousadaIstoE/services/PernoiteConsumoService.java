package com.example.PousadaIstoE.services;

import com.example.PousadaIstoE.exceptions.EntityNotFound;
import com.example.PousadaIstoE.model.EntradaConsumo;
import com.example.PousadaIstoE.model.PernoiteConsumo;
import com.example.PousadaIstoE.model.Pernoites;
import com.example.PousadaIstoE.repository.*;
import com.example.PousadaIstoE.response.EntradaConsumoResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class PernoiteConsumoService {
    private final PernoiteConsumoRepository pernoiteConsumoRepository;
    private final PernoitesRepository pernoitesRepository;

    public PernoiteConsumoService(PernoiteConsumoRepository pernoiteConsumoRepository, PernoitesRepository pernoitesRepository) {
        this.pernoiteConsumoRepository = pernoiteConsumoRepository;

        this.pernoitesRepository = pernoitesRepository;
    }

    public List<PernoiteConsumo> BuscaTodos() {
        return pernoiteConsumoRepository.findAll();
    }

    public PernoiteConsumo addConsumo(PernoiteConsumo pernoiteConsumo) {
        if (pernoiteConsumo.getPernoites() == null) {
            throw new EntityNotFound("Nenhum Pernoite associado a esse consumo");
        }
        PernoiteConsumo pernoiteConsumo1 = new PernoiteConsumo(
                pernoiteConsumo.getQuantidade(),
                pernoiteConsumo.getItens(),
                pernoiteConsumo.getPernoites()
        );
        return pernoiteConsumoRepository.save(pernoiteConsumo1);
    }

    public void deleteConsumoPernoite(Long consumoID){
        pernoiteConsumoRepository.deleteById(consumoID);
    }
}
