package com.example.PousadaIstoE.services;

import com.example.PousadaIstoE.model.County;
import com.example.PousadaIstoE.repository.CountyRepository;
import com.example.PousadaIstoE.response.CountyResponse;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CountyService {

    private final CountyRepository countyRepository;

    public CountyService(CountyRepository countyRepository) {
        this.countyRepository = countyRepository;
    }

    public List<CountyResponse> findAllByStateId(Long state_id){
        var counties = countyRepository.findCountiesByState_Id(state_id);
        List<CountyResponse> countyResponseList = new ArrayList<>();

        counties.forEach(county -> {
            CountyResponse countyResponse = new CountyResponse(
                    county.getId(),
                    county.getDescription()
            );
            countyResponseList.add(countyResponse);
        });
        counties.
        stream().sorted(Comparator
                        .comparingLong(County::getId))
                .collect(Collectors.toList());
        return countyResponseList;
    }
}
