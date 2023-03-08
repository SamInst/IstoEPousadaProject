package com.example.PousadaIstoE.services;

import com.example.PousadaIstoE.model.MapaGeral;
import com.example.PousadaIstoE.repository.MapaGeralRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
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


    public ResponseEntity<ClienteResponse> find(@PathVariable("userId") Long id) {
        final var client = clientRepository.findById(id).orElseThrow(() -> new EntityNotFound("Cliente não encontrado"));
        if (client != null) {
            final var response = new ClienteResponse(
                    client.getNameComplete(),
                    client.getUsername(),
                    client.getCpf(),
                    client.getPhone(),
                    client.getEmail(),
                    client.getAddress(),
                    new ClienteResponse.Nascimento(
                            client.getDataNacimento()
                    ));
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
