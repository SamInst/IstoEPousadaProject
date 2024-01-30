package com.example.PousadaIstoE.controllers;

import com.example.PousadaIstoE.model.County;
import com.example.PousadaIstoE.response.CountyResponse;
import com.example.PousadaIstoE.services.CountyService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/county")
public class CountyController {
    private final CountyService countyService;

    public CountyController(CountyService countyService) {
        this.countyService = countyService;
    }

    @GetMapping("/{state_id}")
    public List<CountyResponse> findAllCountiesByStateId(@PathVariable Long state_id){
       return countyService.findAllByStateId(state_id);
    }
}
