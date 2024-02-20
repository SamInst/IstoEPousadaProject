package com.example.PousadaIstoE.services;

import com.example.PousadaIstoE.exceptions.EntityConflict;
import com.example.PousadaIstoE.model.DailyValues;
import com.example.PousadaIstoE.repository.DailyValueRepository;
import com.example.PousadaIstoE.request.DailyRequest;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
public class DailyValueService {
    private final DailyValueRepository dailyValueRepository;

    public DailyValueService(DailyValueRepository dailyValueRepository) {
        this.dailyValueRepository = dailyValueRepository;
    }

    public List<DailyValues> getAll(){
        return dailyValueRepository.findAll().stream()
                .sorted(Comparator.comparingLong(DailyValues::getId))
                .toList();
    }

    public void insertNewValue(DailyRequest request){
        DailyValues dailyValues = new DailyValues(
                request.amount_people(),
                request.price()
        );
        dailyValueRepository.save(dailyValues);
    }

    public void alterValues(Long daily_id, DailyRequest request){
        var daily = dailyValueRepository.findById(daily_id)
                .orElseThrow(()-> new EntityConflict("Daily not Found"));
        daily.setAmountPeople(request.amount_people());
        daily.setPrice(request.price());
        dailyValueRepository.save(daily);
    }

    public void removeValue(Long daily_id){
        dailyValueRepository.deleteById(daily_id);
    }
}
