package com.example.PousadaIstoE.services;

import com.example.PousadaIstoE.model.County;
import com.example.PousadaIstoE.repository.CountryRepository;
import com.example.PousadaIstoE.repository.CountyRepository;
import com.example.PousadaIstoE.repository.StateRepository;
import com.example.PousadaIstoE.response.PlaceResponse;
import org.springframework.stereotype.Service;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PlaceService {

    private final CountyRepository countyRepository;
    private final CountryRepository countryRepository;
    private final StateRepository stateRepository;

    public PlaceService(CountyRepository countyRepository, CountryRepository countryRepository, StateRepository stateRepository) {
        this.countyRepository = countyRepository;
        this.countryRepository = countryRepository;
        this.stateRepository = stateRepository;
    }

    public List<PlaceResponse> findAllCountiesByStateId(Long stateId) {
        return countyRepository.findCountiesByState_Id(stateId).stream()
                .sorted(Comparator.comparingLong(County::getId))
                .map(place -> new PlaceResponse(
                        place.getId(),
                        place.getDescription()))
                .collect(Collectors.toList());
    }

    public List<PlaceResponse> findAllCountries() {
        return countryRepository.findAll().stream()
                .map(place -> new PlaceResponse(
                        place.getId(),
                        place.getDescription()))
                .collect(Collectors.toList());
    }

    public List<PlaceResponse> findAllStatesByCountryId(Long countryId) {
        return stateRepository.findAllByCountry_Id(countryId).stream()
                .map(place -> new PlaceResponse(
                        place.getId(),
                        place.getDescription()))
                .collect(Collectors.toList());
    }
}
