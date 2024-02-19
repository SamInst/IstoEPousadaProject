package com.example.PousadaIstoE.services;

import com.example.PousadaIstoE.exceptions.EntityNotFound;
import com.example.PousadaIstoE.model.OverNightStayConsumption;
import com.example.PousadaIstoE.repository.*;
import com.example.PousadaIstoE.request.ConsumptionRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OvernightStayConsumptionService {
    private final OvernightStayComsuptionRepository overnightStayComsuptionRepository;
    private final Finder find;

    public OvernightStayConsumptionService(
            OvernightStayComsuptionRepository overnightStayComsuptionRepository,
            Finder find) {
        this.overnightStayComsuptionRepository = overnightStayComsuptionRepository;
        this.find = find;
    }

    public void addConsumption(Long overnight_id, List<ConsumptionRequest> consumptionRequest) {
        consumptionRequest.forEach(request -> {
            var item = find.itemById(request.item_id());
            var overnight = find.overnightStayById(overnight_id);
            OverNightStayConsumption consumption = new OverNightStayConsumption(
                    request.amount(),
                    item,
                    overnight
            );
            overnightStayComsuptionRepository.save(consumption);
        });
    }

    public void removeConsumption(Long consumption_id){
        overnightStayComsuptionRepository.deleteById(consumption_id);
    }
}
