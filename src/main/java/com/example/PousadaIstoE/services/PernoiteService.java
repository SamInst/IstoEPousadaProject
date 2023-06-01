package com.example.PousadaIstoE.services;

import com.example.PousadaIstoE.exceptions.EntityConflict;
import com.example.PousadaIstoE.exceptions.EntityDates;
import com.example.PousadaIstoE.exceptions.EntityNotFound;
import com.example.PousadaIstoE.model.Pernoites;
import com.example.PousadaIstoE.model.Quartos;
import com.example.PousadaIstoE.repository.PernoitesRepository;
import com.example.PousadaIstoE.repository.QuartosRepository;
import com.example.PousadaIstoE.response.PernoiteResponse;
import com.example.PousadaIstoE.response.StatusDoQuarto;
import com.example.PousadaIstoE.response.StatusPagamento;
import com.example.PousadaIstoE.response.TipoPagamento;
import org.springframework.beans.BeanUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Period;
import java.time.chrono.ChronoLocalDate;
import java.util.List;

@Service
public class PernoiteService {
    Float price;
    private final PernoitesRepository pernoitesRepository;
    private final QuartosRepository quartosRepository;



    protected PernoiteService(PernoitesRepository pernoitesRepository, QuartosRepository quartosRepository) {
        this.pernoitesRepository = pernoitesRepository;

        this.quartosRepository = quartosRepository;
    }

    public List<Pernoites> findAll(){
        return pernoitesRepository.findAll();
    }

    public ResponseEntity<PernoiteResponse> findById(Long id){
        final var pernoites = pernoitesRepository.findById(id).orElseThrow(() -> new EntityNotFound("Pernoite não encontrado"));

        quantidadeDePessoas(pernoites);

        Integer p1 = Period.between(pernoites.getDataEntrada(), pernoites.getDataSaida()).getDays();

        Float total = (price * p1);

        final var response = new  PernoiteResponse(
                new PernoiteResponse.Client(
                        pernoites.getClient().getName(),
                        pernoites.getClient().getPhone()
                ),
                        pernoites.getApartamento().getNumero(),
                        pernoites.getDataEntrada(),
                        pernoites.getDataSaida(),
               new PernoiteResponse.Valores(
                        pernoites.getQuantidadePessoa(),
                        p1,
                        price,
                        total,
                        pernoites.getTipoPagamento(),
                        pernoites.getStatus_pagamento()
               ),
                pernoites.getApartamento().getStatusDoQuarto()
        );
        return ResponseEntity.ok(response);
    }

    public Pernoites createPernoite(Pernoites pernoites){
        validacaoDeApartamento(pernoites);
        if (pernoites.getStatus_pagamento() == null){
            pernoites.setStatus_pagamento(StatusPagamento.PENDENTE);
        }
        if (pernoites.getTipoPagamento() == null){
            pernoites.setTipoPagamento(TipoPagamento.PENDENTE);
        }
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
        List<Pernoites> pernoitesCadastrados = pernoitesRepository.findByApartamento_Id(pernoite.getApartamento().getId());
        if (pernoite.getDataEntrada().isBefore(LocalDate.now())
                || pernoite.getDataSaida().isBefore(LocalDate.now())){
            throw new EntityDates("A data inserida não pode ser inferior a hoje");
        }
        if (pernoite.getApartamento().getStatusDoQuarto().equals(StatusDoQuarto.OCUPADO)){
            throw new EntityConflict("O apartamento já está ocupado.");
        }
        for (Pernoites pernoiteCadastrado : pernoitesCadastrados) {

            if (pernoite.getDataEntrada().isBefore(pernoiteCadastrado.getDataSaida())
                    && pernoite.getDataSaida().isAfter(pernoiteCadastrado.getDataEntrada())) {
                throw new EntityConflict("O apartamento já está ocupado entre as datas informadas.");
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

    @Scheduled(cron = "0 * * * * ?") // Executa todos os dias à meia-noite
    public void updateRoomStatus() {
        LocalDate currentDate = LocalDate.now();
        List<Pernoites> pernoitesToCheckIn = pernoitesRepository.findByDataEntrada(currentDate);

        for (Pernoites pernoite : pernoitesToCheckIn) {
            if (pernoite.getDataEntrada().equals(currentDate)){
                Quartos quarto = pernoite.getApartamento();
                quarto.setStatusDoQuarto(StatusDoQuarto.OCUPADO);
                quartosRepository.save(quarto);
            }
            if (pernoite.getDataSaida().equals(currentDate) && LocalTime.now().isAfter(LocalTime.of(12,0))){
                Quartos quarto = pernoite.getApartamento();
                quarto.setStatusDoQuarto(StatusDoQuarto.DISPONIVEL);
                quartosRepository.save(quarto);
            }
        }
    }
}