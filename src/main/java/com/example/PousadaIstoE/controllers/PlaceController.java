package com.example.PousadaIstoE.controllers;

import com.example.PousadaIstoE.response.PlaceResponse;
import com.example.PousadaIstoE.services.PlaceService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/place")
public class PlaceController {
    private final PlaceService placeService;

    public PlaceController(PlaceService placeService) {
        this.placeService = placeService;
    }

    @GetMapping("/county/{state_id}")
    public List<PlaceResponse> findAllCountiesByStateId(@PathVariable Long state_id){
       return placeService.findAllCountiesByStateId(state_id);
    }
}
