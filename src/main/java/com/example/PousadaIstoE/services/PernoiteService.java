package com.example.PousadaIstoE.services;

import com.example.PousadaIstoE.exceptions.EntityConflict;
import com.example.PousadaIstoE.exceptions.EntityNotFound;
import com.example.PousadaIstoE.model.Pernoites;
import com.example.PousadaIstoE.repository.PernoitesRepository;
import com.example.PousadaIstoE.response.PernoiteResponse;
import org.springframework.beans.BeanUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.time.chrono.ChronoLocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
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

    public ResponseEntity<PernoiteResponse> findById(Long id){
        final var pernoites = pernoitesRepository.findById(id).orElseThrow(() -> new EntityNotFound("Pernoite não encontrado"));
        System.out.println(pernoites.getQuantidadePessoa());
        System.out.println(pernoites.getApt());

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
        validacaoPernoite(pernoites);
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

    private void validacaoPernoite(Pernoites pernoites){
        List<Pernoites> pernoitesExistentes = pernoitesRepository.findAll();
        List<LocalDate> listaDias = listagemDeDiasEntreDatas(pernoites.getDataEntrada(), pernoites.getDataSaida());

        List<LocalDate> dia2 = new ArrayList<>();
        dia2.add(pernoites.getDataEntrada());


//        for (LocalDate dia : listaDias) {
//            System.out.println(dia + " asda");
//        }
        for (Pernoites pernoite : pernoitesExistentes) {
            for (LocalDate dia : listaDias) {
                System.out.println(dia + " asda");

                System.out.println(listaDias + " bbb");
//                if (pernoite.getDataEntrada().isEqual(dia)) {
//                    throw new EntityConflict("O quarto não está disponível nessa data.");
//                }
            if (pernoite.getApt().equals(pernoites.getApt())) {
                if (pernoites.getDataEntrada().isEqual(pernoite.getDataEntrada())
                    ||
                    pernoites.getDataSaida().isEqual(pernoite.getDataSaida())
                    ||
                    dia2.equals(listaDias)
                ) {
                   throw new EntityConflict("O quarto não está disponível nessa data.");
                    }
                }
            }
        }
    }

    private static List<LocalDate> listagemDeDiasEntreDatas(LocalDate dataInicial, LocalDate dataFinal) {
        List<LocalDate> listaDias = new ArrayList<>();
        long diferencaEmDias = ChronoUnit.DAYS.between(dataInicial, dataFinal);

        for (int i = 0; i <= diferencaEmDias; i++) {
            LocalDate data = dataInicial.plusDays(i);
            listaDias.add(data);
        }
        System.out.println(listaDias);
        return listaDias;
    }
}