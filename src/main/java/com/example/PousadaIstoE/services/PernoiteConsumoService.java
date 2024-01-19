package com.example.PousadaIstoE.services;

import com.example.PousadaIstoE.exceptions.EntityNotFound;
import com.example.PousadaIstoE.model.OverNightStayConsumption;
import com.example.PousadaIstoE.repository.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PernoiteConsumoService {
    private final PernoiteConsumoRepository pernoiteConsumoRepository;
    private final PernoitesRepository pernoitesRepository;

    public PernoiteConsumoService(PernoiteConsumoRepository pernoiteConsumoRepository, PernoitesRepository pernoitesRepository) {
        this.pernoiteConsumoRepository = pernoiteConsumoRepository;

        this.pernoitesRepository = pernoitesRepository;
    }

    public List<OverNightStayConsumption> BuscaTodos() {
        return pernoiteConsumoRepository.findAll();
    }

    public OverNightStayConsumption addConsumo(OverNightStayConsumption overNightStayConsumption) {
        if (overNightStayConsumption.getPernoites() == null) {
            throw new EntityNotFound("Nenhum Pernoite associado a esse consumo");
        }
        OverNightStayConsumption overNightStayConsumption1 = new OverNightStayConsumption(
                overNightStayConsumption.getQuantidade(),
                overNightStayConsumption.getItens(),
                overNightStayConsumption.getPernoites()
        );
        return pernoiteConsumoRepository.save(overNightStayConsumption1);
    }

    public void deleteConsumoPernoite(Long consumoID){
        pernoiteConsumoRepository.deleteById(consumoID);
    }
}
