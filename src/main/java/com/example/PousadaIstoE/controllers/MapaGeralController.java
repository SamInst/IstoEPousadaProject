package com.example.PousadaIstoE.controllers;

import com.example.PousadaIstoE.model.MapaGeral;
import com.example.PousadaIstoE.response.MapaGeralResponse;
import com.example.PousadaIstoE.services.MapaGeralService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/maps")
public class MapaGeralController {

private final MapaGeralService mapaGeralService;

    public MapaGeralController(MapaGeralService mapaGeralService) {
        this.mapaGeralService = mapaGeralService;
    }

    @GetMapping
public List<MapaGeral> list(){
    return mapaGeralService.listAllMaps();
}
//@GetMapping
public ResponseEntity<MapaGeralResponse> findMaps(Long id, Float entry, Float out){
    return mapaGeralService.findMapaGeral(id, entry, out);
}

}
