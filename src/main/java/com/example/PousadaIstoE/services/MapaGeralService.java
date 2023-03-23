package com.example.PousadaIstoE.services;

import com.example.PousadaIstoE.exceptions.EntityNotFound;
import com.example.PousadaIstoE.model.MapaGeral;
import com.example.PousadaIstoE.repository.MapaGeralRepository;
import com.example.PousadaIstoE.response.MapaGeralResponse;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
public class MapaGeralService {
    @PersistenceContext
    private EntityManager manager;
    private final MapaGeralRepository mapaGeralRepository;
    public MapaGeralService(MapaGeralRepository mapaGeralRepository) {
        this.mapaGeralRepository = mapaGeralRepository;
    }

    public List<MapaGeral> listAllMaps(){
        return mapaGeralRepository.findAll();
    }

    public MapaGeral add(MapaGeral mapaGeral) {
        mapaGeral.setData(LocalDate.now());
        mapaGeral.setHora(LocalTime.now());
        mapaGeral.setTotal(total(mapaGeral));
        return mapaGeralRepository.save(mapaGeral);
    }

    public ResponseEntity<MapaGeralResponse> findMapaGeral(Long id) {
        final var mapaGeral = mapaGeralRepository.findById(id).orElseThrow(() -> new EntityNotFound("Mapa não encontrado"));
        final var response = new MapaGeralResponse(
                mapaGeral.getData(),
                mapaGeral.getHora(),
                mapaGeral.getReport(),
                mapaGeral.getApartment(),
                mapaGeral.getEntrada(),
                mapaGeral.getSaida(),
                mapaGeral.getTotal()
        );
        return ResponseEntity.ok(response);
    }

  private Float total(MapaGeral mapaGeral){
      Float total = manager.createQuery("SELECT m.total FROM MapaGeral m ORDER BY m.id DESC", Float.class)
              .setMaxResults(1)
              .getSingleResult();
      Float entrada = mapaGeral.getEntrada();
      Float saida =  mapaGeral.getSaida();
      Float somaTotal = total + entrada - saida;
      mapaGeral.setTotal(somaTotal);
      return mapaGeral.getTotal();
  }
}