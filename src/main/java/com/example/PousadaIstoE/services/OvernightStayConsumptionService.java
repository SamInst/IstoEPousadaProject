package com.example.PousadaIstoE.services;

import com.example.PousadaIstoE.model.OverNightStayConsumption;
import com.example.PousadaIstoE.repository.OverNightStayConsumptionRepository;
import com.example.PousadaIstoE.request.ConsumptionRequest;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class OvernightStayConsumptionService {
    private final Finder find;
    private final OverNightStayConsumptionRepository overNightStayConsumptionRepository;

    public OvernightStayConsumptionService(
            Finder find, OverNightStayConsumptionRepository overNightStayConsumptionRepository) {
        this.find = find;
        this.overNightStayConsumptionRepository = overNightStayConsumptionRepository;
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
            overNightStayConsumptionRepository.save(consumption);
        });
    }

    public void removeConsumption(Long consumption_id){
        overNightStayConsumptionRepository.deleteById(consumption_id);
    }
}
