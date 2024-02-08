package com.example.PousadaIstoE.controllers;

import com.example.PousadaIstoE.response.PlaceResponse;
import com.example.PousadaIstoE.services.PlaceService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/place")
public class PlaceController {
    private final PlaceService placeService;

    public PlaceController(PlaceService placeService) {
        this.placeService = placeService;
    }

    @GetMapping("/county/{state_id}")
    @ResponseStatus(HttpStatus.OK)
    public List<PlaceResponse> findAllCountiesByStateId(@PathVariable Long state_id){
       return placeService.findAllCountiesByStateId(state_id);
    }

    @GetMapping("/countries")
    @ResponseStatus(HttpStatus.OK)
    public List<PlaceResponse> findAllCountries() {
       return placeService.findAllCountries();
    }

    @GetMapping("/states/{country_id}")
    @ResponseStatus(HttpStatus.OK)
    public List<PlaceResponse> findAllStates(@PathVariable Long country_id) {
        return placeService.findAllStatesByCountryId(country_id);
    }

}
