package com.example.PousadaIstoE.services;

import com.example.PousadaIstoE.exceptions.EntityNotFound;
import com.example.PousadaIstoE.model.OverNightStayConsumption;
import com.example.PousadaIstoE.repository.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OvernightStayConsumptionService {
    private final OvernightStayComsuptionRepository overnightStayComsuptionRepository;
    private final OvernightStayRepository overnightStayRepository;

    public OvernightStayConsumptionService(OvernightStayComsuptionRepository overnightStayComsuptionRepository, OvernightStayRepository overnightStayRepository) {
        this.overnightStayComsuptionRepository = overnightStayComsuptionRepository;

        this.overnightStayRepository = overnightStayRepository;
    }

    public List<OverNightStayConsumption> BuscaTodos() {
        return overnightStayComsuptionRepository.findAll();
    }

    public OverNightStayConsumption addConsumo(OverNightStayConsumption overNightStayConsumption) {
        if (overNightStayConsumption.getPernoites() == null) {
            throw new EntityNotFound("Nenhum Pernoite associado a esse consumption");
        }
        OverNightStayConsumption overNightStayConsumption1 = new OverNightStayConsumption(
                overNightStayConsumption.getAmount(),
                overNightStayConsumption.getItens(),
                overNightStayConsumption.getPernoites()
        );
        return overnightStayComsuptionRepository.save(overNightStayConsumption1);
    }

    public void deleteConsumoPernoite(Long consumoID){
        overnightStayComsuptionRepository.deleteById(consumoID);
    }
}
