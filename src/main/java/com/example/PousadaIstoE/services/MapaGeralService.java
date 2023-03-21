package com.example.PousadaIstoE.services;

import com.example.PousadaIstoE.exceptions.EntityNotFound;
import com.example.PousadaIstoE.model.MapaGeral;
import com.example.PousadaIstoE.repository.MapaGeralRepository;
import com.example.PousadaIstoE.response.MapaGeralResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Service
public class MapaGeralService {
    private final MapaGeralRepository mapaGeralRepository;

    public MapaGeralService(MapaGeralRepository mapaGeralRepository) {
        this.mapaGeralRepository = mapaGeralRepository;
    }

    public List<MapaGeral> listAllMaps(){
        return mapaGeralRepository.findAll();
    }


    public ResponseEntity<MapaGeralResponse> findMapaGeral(@PathVariable("userId") Long id, Float entry, Float out) {
        final var mapaGeral = mapaGeralRepository.findById(id).orElseThrow(() -> new EntityNotFound("Mapa não encontrado"));
        Float total = mapaGeral.getEntrada()+ mapaGeral.getSaida();

        final var total = mapaGeralRepository.findById(mapaGeral.getId()).

        final var response = new MapaGeralResponse(
                mapaGeral.getData(),
                mapaGeral.getReport(),
                mapaGeral.getApartment(),
                mapaGeral.getEntrada(),
                mapaGeral.getSaida(),
                total
        );
        return ResponseEntity.ok(response);
    }

}
