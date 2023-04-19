package com.example.PousadaIstoE.services;

import com.example.PousadaIstoE.exceptions.EntityConflict;
import com.example.PousadaIstoE.exceptions.EntityDates;
import com.example.PousadaIstoE.exceptions.EntityNotFound;
import com.example.PousadaIstoE.model.ConsumoPernoite;
import com.example.PousadaIstoE.model.Pernoites;
import com.example.PousadaIstoE.repository.PernoitesRepository;
import com.example.PousadaIstoE.response.PernoiteResponse;
import org.springframework.beans.BeanUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;

@Service
public class PernoiteService {
    Float price;
    private final PernoitesRepository pernoitesRepository;

    protected PernoiteService(PernoitesRepository pernoitesRepository) {
        this.pernoitesRepository = pernoitesRepository;
    }

    public List<Pernoites> findAll(){
        return pernoitesRepository.findAll();
    }

    public ResponseEntity<PernoiteResponse> findById(Long id){
        final var pernoites = pernoitesRepository.findById(id).orElseThrow(() -> new EntityNotFound("Pernoite não encontrado"));
        System.out.println(pernoites.getQuantidadePessoa());
        System.out.println(pernoites.getApt());

        quantidadeDePessoas(pernoites);

        Integer p1 = Period.between(pernoites.getDataEntrada(), pernoites.getDataSaida()).getDays();
        List<ConsumoPernoite> consum = pernoites.getConsumoList();
        Float total = (price * p1) + consum.size();
        System.out.println(consum.size()+" valor aguas");

        final var response = new  PernoiteResponse(
                new PernoiteResponse.Client(
                        pernoites.getClient().getName(),
                        pernoites.getClient().getPhone()
                ),
                        pernoites.getApt(),
                        pernoites.getDataEntrada(),
                        pernoites.getDataSaida(),
                        pernoites.getConsumoList(),
               new PernoiteResponse.Valores(
                        pernoites.getQuantidadePessoa(),
                        p1,
                        price,
                        total,
                       pernoites.getTipoPagamento(),
                       pernoites.getStatus_pagamento()
               )
        );
        return ResponseEntity.ok(response);
    }

    public Pernoites createPernoite(Pernoites pernoites){
        validacaoDeApartamento(pernoites);
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

    private void validacaoDeApartamento(Pernoites pernoite) throws EntityConflict {
        List<Pernoites> pernoitesCadastrados = pernoitesRepository.findByApt(pernoite.getApt());

        for (Pernoites pernoiteCadastrado : pernoitesCadastrados) {
            if (pernoite.getDataEntrada().isBefore(pernoiteCadastrado.getDataSaida())
                    && pernoite.getDataSaida().isAfter(pernoiteCadastrado.getDataEntrada())) {
                throw new EntityConflict("O apartamento já está ocupado entre as datas informadas.");
            }
            if (pernoite.getDataEntrada().isBefore(LocalDate.now())
                    || pernoite.getDataSaida().isBefore(LocalDate.now())){
                throw new EntityDates("A data inserida não pode ser inferior a hoje");
            }
            if (pernoite.getDataSaida().isBefore(pernoite.getDataEntrada())){
                throw new EntityDates("A data de Saída não pode ser inferior a data de entrada");
            }
            if (pernoite.getDataEntrada().equals(pernoite.getDataSaida())
                    ||pernoite.getDataSaida().equals(pernoite.getDataEntrada())) {
                throw new EntityDates("A data de Entrada/Saída não podem ser iguais");
            }
        }
        pernoitesRepository.save(pernoite);
    }
}