package com.example.PousadaIstoE.services;

import com.example.PousadaIstoE.exceptions.EntityNotFound;
import com.example.PousadaIstoE.model.CashRegister;
import com.example.PousadaIstoE.repository.MapaGeralRepository;
import com.example.PousadaIstoE.response.CashRegisterResponse;
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
    public List<CashRegister> listAllMaps(){
        return mapaGeralRepository.findAll();
    }

    public CashRegister createMapa(CashRegister cashRegister) {
        Float total = mapaGeralRepository.findLastTotal();

        if (total == null){
            throw new EntityNotFound("Não foi criado um mapa hoje ainda");
        }
        cashRegister.setData(LocalDate.now());
        cashRegister.setHora(LocalTime.now());
        cashRegister.setTotal(total(cashRegister));
        return mapaGeralRepository.save(cashRegister);
    }

    public ResponseEntity<CashRegisterResponse> findMapaGeral(Long id) {
        final var mapaGeral = mapaGeralRepository.findById(id).orElseThrow(() -> new EntityNotFound("Mapa não encontrado"));
        final var response = new CashRegisterResponse(
                mapaGeral.getData(),
                mapaGeral.getHour(),
                mapaGeral.getReport(),
                mapaGeral.getApartment(),
                mapaGeral.getCashIn(),
                mapaGeral.getCashOut(),
                mapaGeral.getTotal()
        );
        return ResponseEntity.ok(response);
    }

  private Float total(CashRegister cashRegister){
      Float total = manager.createQuery("SELECT m.total FROM CashRegister m ORDER BY m.id DESC", Float.class)
              .setMaxResults(1)
              .getSingleResult();
      Float entrada = cashRegister.getCashIn();
      Float saida =  cashRegister.getCashOut();
      Float somaTotal = total + entrada - saida;
      cashRegister.setTotal(somaTotal);
      return cashRegister.getTotal();
  }

  public List<CashRegister> findByData(LocalDate date){
      return mapaGeralRepository.findByData(date);
  }

  public Float totalMapaGeral(){
    return mapaGeralRepository.findLastTotal();
  }
}