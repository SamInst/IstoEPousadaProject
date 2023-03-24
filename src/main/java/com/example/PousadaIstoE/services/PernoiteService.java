package com.example.PousadaIstoE.services;

import com.example.PousadaIstoE.exceptions.EntityConflict;
import com.example.PousadaIstoE.exceptions.EntityNotFound;
import com.example.PousadaIstoE.model.Pernoites;
import com.example.PousadaIstoE.repository.PernoitesRepository;
import com.example.PousadaIstoE.response.PernoiteResponse;
import org.springframework.beans.BeanUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.Period;
import java.util.List;

@Service
public class PernoiteService {
    Float price;
    private final PernoitesRepository pernoitesRepository;

    public PernoiteService(PernoitesRepository pernoitesRepository) {
        this.pernoitesRepository = pernoitesRepository;
    }

    public List<Pernoites> findAll(){
        return pernoitesRepository.findAll();
    }

    public ResponseEntity<PernoiteResponse> findbyId(Long id){
        final var pernoites = pernoitesRepository.findById(id).orElseThrow(() -> new EntityNotFound("Pernoite não encontrado"));
        quantidadeDePessoas(pernoites);
        Integer p1 = Period.between(pernoites.getDataEntrada(), pernoites.getDataSaida()).getDays();
        Float total = price * p1;

        final var response = new  PernoiteResponse(
                new PernoiteResponse.Client(
                        pernoites.getClient().getName(),
                        pernoites.getClient().getPhone()
                ),
                pernoites.getApt(),
                pernoites.getDataEntrada(),
                pernoites.getDataSaida(),
                pernoites.getConsumo(),
               new PernoiteResponse.Valores(
                       pernoites.getQuantidadePessoa(),
                       p1,
                       price,
                       total
               )
        );
        return ResponseEntity.ok(response);
    }

    public Pernoites createPernoite(Pernoites pernoites){
        return pernoitesRepository.save(pernoites);
    }

    public Pernoites updatePernoiteData(Long pernoiteId, Pernoites pernoites) {
        Pernoites pernoites1 = pernoitesRepository.findById(pernoiteId).get();
        BeanUtils.copyProperties(pernoites1, pernoites, "id");
        return pernoitesRepository.save(pernoites1);
    }

    private void quantidadeDePessoas (Pernoites pernoites) {
        Integer quantidadePessoa = pernoites.getQuantidadePessoa();
        switch (quantidadePessoa){
            case 1  -> price = 90F;
            case 2  -> price = 130F;
            case 3  -> price = 180F;
            case 4  -> price = 220F;
            case 5  -> price = 280F;
            default -> throw new EntityConflict("Não podem ser inseridos mais de 5 pessoas no mesmo quarto");
         }
    }
}
