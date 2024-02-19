package com.example.PousadaIstoE.services;

import com.example.PousadaIstoE.model.EntryConsumption;
import com.example.PousadaIstoE.repository.EntryConsumptionRepository;
import com.example.PousadaIstoE.request.ConsumptionRequest;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class EntryConsumptionService {
    private final EntryConsumptionRepository entryConsumptionRepository;
    private final Finder find;
    public EntryConsumptionService(
            EntryConsumptionRepository entryConsumptionRepository,
            Finder find) {
        this.entryConsumptionRepository = entryConsumptionRepository;
        this.find = find;
    }

    public void addConsumptionToEntry(Long entry_id, List<ConsumptionRequest> request) {
        request.forEach(newItem -> {
            var item = find.itemById(newItem.item_id());
            var entry = find.entryById(entry_id);

            EntryConsumption entryConsumption = new EntryConsumption(
                    newItem.amount(),
                    item,
                    entry);
            entryConsumptionRepository.save(entryConsumption);
        });
    }

    public void removeConsumption(Long consumption_id) {
        entryConsumptionRepository.deleteById(consumption_id);
    }
}
