package com.example.PousadaIstoE.services;

import com.example.PousadaIstoE.model.EntryConsumption;
import com.example.PousadaIstoE.repository.EntryConsumptionRepository;
import com.example.PousadaIstoE.request.EntryConsumptionRequest;
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

    public void addConsumption(Long entry_id, EntryConsumptionRequest request) {
        var item = find.itemById(request.item_id());
        var entry = find.entryById(entry_id);

        EntryConsumption entryConsumption = new EntryConsumption(request.amount(), item, entry);
        entryConsumptionRepository.save(entryConsumption);
    }

    public void removeConsumption(Long consumption_id) {
        entryConsumptionRepository.deleteById(consumption_id);
    }
}
