package com.example.PousadaIstoE.services;

import com.example.PousadaIstoE.exceptions.EntityConflict;
import com.example.PousadaIstoE.exceptions.EntityDates;
import com.example.PousadaIstoE.exceptions.EntityNotFound;
import com.example.PousadaIstoE.model.*;
import com.example.PousadaIstoE.repository.AcompanhantePernoiteRepository;
import com.example.PousadaIstoE.repository.PernoiteConsumoRepository;
import com.example.PousadaIstoE.repository.PernoitesRepository;
import com.example.PousadaIstoE.repository.QuartosRepository;
import com.example.PousadaIstoE.response.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import static java.time.Period.*;

@Service
public class PernoiteService {
    Float price;
    @PersistenceContext
    private EntityManager manager;
    private final PernoitesRepository pernoitesRepository;
    private final QuartosRepository quartosRepository;
    private final MapaGeralService mapaGeralService;
    private final PernoiteConsumoRepository pernoiteConsumoRepository;
    private final AcompanhantePernoiteRepository acompanhantePernoiteRepository;

    protected PernoiteService(PernoitesRepository pernoitesRepository, QuartosRepository quartosRepository, MapaGeralService mapaGeralService, PernoiteConsumoRepository pernoiteConsumoRepository, AcompanhantePernoiteRepository acompanhantePernoiteRepository) {
        this.pernoitesRepository = pernoitesRepository;
        this.quartosRepository = quartosRepository;
        this.mapaGeralService = mapaGeralService;
        this.pernoiteConsumoRepository = pernoiteConsumoRepository;
        this.acompanhantePernoiteRepository = acompanhantePernoiteRepository;
    }

    public List<PernoiteShortResponse> findAll() {
        var allPernoites =  pernoitesRepository.findAll();
        List<PernoiteShortResponse> pernoiteShortResponseList = new ArrayList<>();

        allPernoites.forEach(pernoite->{
            PernoiteShortResponse pernoiteShortResponse = new PernoiteShortResponse(
                    pernoite.getId(),
                    new PernoiteShortResponse.Client(pernoite.getClient().getName()),
                    pernoite.getApartamento().getNumero(),
                    pernoite.getDataEntrada(),
                    pernoite.getDataSaida(),
                    pernoite.getQuantidadePessoa()
            );
            pernoiteShortResponseList.add(pernoiteShortResponse);
        });
        return pernoiteShortResponseList;
    }

    public PernoiteResponse findById(Long id) {
        final var pernoites = pernoitesRepository.findById(id).orElseThrow(() -> new EntityNotFound("Pernoite não encontrado"));
        final var consumo_pernoite = pernoiteConsumoRepository.findPernoiteConsumoByPernoites_Id(id);
        final var totalConsumo = pernoiteConsumoRepository.findConsumoTotal(id);
        final var acompanhantes = acompanhantePernoiteRepository.findAllByPernoites_Id(id);
        quantidadeDePessoas2(pernoites);

        List<AcompanhantePernoiteShortResponse> acompanhantePernoiteShortResponseList = new ArrayList<>();
        acompanhantes.forEach(acompanhantePernoite -> {
            AcompanhantePernoiteShortResponse acompanhantePernoiteShortResponse = new AcompanhantePernoiteShortResponse(
                    acompanhantePernoite.getId(),
                    acompanhantePernoite.getName(),
                    acompanhantePernoite.getCpf(),
                    acompanhantePernoite.getAge()
            );
            acompanhantePernoiteShortResponseList.add(acompanhantePernoiteShortResponse);
        });
        Integer p1 = between(pernoites.getDataEntrada(), pernoites.getDataSaida()).getDays();
        Float total_diarias = pernoites.getTotal() * p1;
        var valor_total = valorTotal(id, pernoites, p1, total_diarias);


        return new PernoiteResponse(
                pernoites.getId(),
                new PernoiteResponse.Client(
                        pernoites.getClient().getName(),
                        pernoites.getClient().getCpf(),
                        pernoites.getClient().getPhone()
                ),
                acompanhantePernoiteShortResponseList,
                new PernoiteResponse.Quarto(pernoites.getApartamento().getNumero()),
                pernoites.getDataEntrada(),
                pernoites.getDataSaida(),
                consumo_pernoite,
                new PernoiteResponse.Valores(
                        pernoites.getQuantidadePessoa(),
                        p1,
                        pernoites.getTotal(),
                        totalConsumo,
                        total_diarias,
                        pernoites.getTipoPagamento(),
                        pernoites.getStatus_pagamento(),
                        valor_total
                )
        );
    }

    public Double valorTotal(Long id, Pernoites pernoites, Integer p1, Float total_diarias){
        Double totalConsumo = manager.createQuery(
        "SELECT sum(m.total) FROM PernoiteConsumo m where m.pernoites.id = :id", Double.class)
        .setParameter("id", id)
        .getSingleResult(); if (totalConsumo == null){ totalConsumo = 0D; }
        return total_diarias + totalConsumo;
    }

    public Pernoites createPernoite(Pernoites pernoites) {
        if (pernoites.getClient() == null){
            throw new EntityConflict("É preciso informar o hóspede.");
        }
        Integer periodoDias = between(pernoites.getDataEntrada(), pernoites.getDataSaida()).getDays();

        quantidadeDePessoas2(pernoites);
        validacaoDeApartamento(pernoites);
        Float a = pernoites.getTotal() * periodoDias;
        pernoites.setTotal(a);
        if (pernoites.getStatus_pagamento() == null) {
            pernoites.setStatus_pagamento(StatusPagamento.PENDENTE);
        }
        if (pernoites.getTipoPagamento() == null) {
            pernoites.setTipoPagamento(TipoPagamento.PENDENTE);
        }
        return pernoitesRepository.save(pernoites);
    }

    public Pernoites updatePernoiteData(Long pernoiteId, Pernoites request) {
        Pernoites pernoite = pernoitesRepository.findById(pernoiteId)
                .orElseThrow(()-> new EntityNotFound("Pernoite não encontrado"));

        var pernoiteAtualizado = new Pernoites(
            pernoite.getId(),
            pernoite.getApartamento(),
            pernoite.getClient(),
            pernoite.getDataEntrada(),
            pernoite.getDataSaida(),
            pernoite.getQuantidadePessoa(),
            request.getTipoPagamento(),
            request.getStatus_pagamento(),
            pernoite.getTotal()
        );


        return pernoitesRepository.save(pernoiteAtualizado);
    }

//    private void validacaoPagamento(Pernoites request){
//        Float totalMapaGeral = mapaGeralService.totalMapaGeral();
//        Double totalConsumo = pernoiteConsumoRepository.findConsumoTotal(request.getId());
//        if (totalConsumo == null){ totalConsumo = 0D; }
//        entradaEConsumo = valorEntrada + totalConsumo;
//        valorTotal = totalMapaGeral + entradaEConsumo;
//    }

    private void quantidadeDePessoas2(Pernoites pernoites) {
        Integer quantidadePessoa = pernoites.getQuantidadePessoa();
        switch (quantidadePessoa) {
            case 1 -> pernoites.setTotal(90F);
            case 2 -> pernoites.setTotal(130F);
            case 3 -> pernoites.setTotal(180F);
            case 4 -> pernoites.setTotal(220F);
            case 5 -> pernoites.setTotal(280F);
            default -> throw new EntityConflict("Não podem ser inseridos mais de 5 pessoas no mesmo quarto");
        }
    }

    private void validacaoDeApartamento(Pernoites pernoite) throws EntityConflict {
        List<Pernoites> pernoitesCadastrados = pernoitesRepository.findByApartamento_Id(pernoite.getApartamento().getId());
        if (pernoite.getDataEntrada().isBefore(LocalDate.now()) || pernoite.getDataSaida().isBefore(LocalDate.now())) {
            throw new EntityDates("A data inserida não pode ser inferior a hoje");
        }
        switch (pernoite.getApartamento().getStatusDoQuarto()){
            case OCUPADO    -> throw new EntityConflict("O apartamento já está ocupado.");
            case LIMPEZA    -> throw new EntityConflict("O apartamento necessita de limpeza.");
            case RESERVADO  -> throw new EntityConflict("O apartamento está reservado.");
            case MANUTENCAO -> throw new EntityConflict("O apartamento está em manutenção.");
        }
        for (Pernoites pernoiteCadastrado : pernoitesCadastrados) {
            if (pernoite.getDataEntrada().isBefore(pernoiteCadastrado.getDataSaida()) && pernoite.getDataSaida().isAfter(pernoiteCadastrado.getDataEntrada())) {
                throw new EntityConflict("O apartamento já está ocupado entre as datas informadas.");
            }
            if (pernoite.getDataSaida().isBefore(pernoite.getDataEntrada())) {
                throw new EntityDates("A data de Saída não pode ser inferior a data de entrada");
            }
            if (pernoite.getDataEntrada().equals(pernoite.getDataSaida()) || pernoite.getDataSaida().equals(pernoite.getDataEntrada())) {
                throw new EntityDates("A data de Entrada/Saída não podem ser iguais");
            }
        }
        pernoitesRepository.save(pernoite);
    }

    public AcompanhantePernoite addAcompanhante(AcompanhantePernoite acompanhantePernoite){
        var b = acompanhantePernoite.getBirth();
        Period age = Period.ofYears(Period.between(LocalDate.now(), b).getYears());
        acompanhantePernoite.setAge(age.getYears());
        return acompanhantePernoiteRepository.save(acompanhantePernoite);
    }

    @Scheduled(cron = "0 * * * * ?") // Tem que executar todos os dias à meia-noite
    public void updateRoomStatus() {
        LocalDate currentDate = LocalDate.now();
        List<Pernoites> pernoitesToCheckIn = pernoitesRepository.findByDataEntrada(currentDate);

        for (Pernoites pernoiteIn : pernoitesToCheckIn) {
            Quartos quartoIn = pernoiteIn.getApartamento();

            if (pernoiteIn.getDataEntrada().equals(currentDate)
                    && LocalTime.now().isAfter(LocalTime.of(6,0))
                    && LocalTime.now().isBefore(LocalTime.of(12,0))){
                quartoIn.setStatusDoQuarto(StatusDoQuarto.RESERVADO);
                quartosRepository.save(quartoIn);
            }
            if (pernoiteIn.getDataEntrada().equals(currentDate)
            && LocalTime.now().isAfter(LocalTime.of(12,0))
            ){
                quartoIn.setStatusDoQuarto(StatusDoQuarto.OCUPADO);
                quartosRepository.save(quartoIn);
            }
        }
    }

    @Scheduled(cron = "0 1 12 * * ?")
    public void updateRoomStatusOut() {
        String relatorio = "Pernoite";
        LocalDate currentDate = LocalDate.now();
        List<Pernoites> pernoitesToCheckOut = pernoitesRepository.findByDataSaida(currentDate);
        MapaGeral mapaGeral = new MapaGeral();

        for (Pernoites pernoiteOut : pernoitesToCheckOut) {
            Quartos quartoOut = pernoiteOut.getApartamento();
            if (pernoiteOut.getDataSaida().equals(currentDate) && LocalTime.now().isAfter(LocalTime.of(12, 0))) {
                quartoOut.setStatusDoQuarto(StatusDoQuarto.DIARIA_ENCERRADA);
                quartosRepository.save(quartoOut);
            }
            if (pernoiteOut.getDataSaida().equals(currentDate)
                    && pernoiteOut.getStatus_pagamento().equals(StatusPagamento.CONCLUIDO)) {
                if (pernoiteOut.getTipoPagamento().equals(TipoPagamento.PIX)) {
                    relatorio += " (PIX)";
                }
                if (pernoiteOut.getTipoPagamento().equals(TipoPagamento.CARTAO)) {
                    relatorio += " (CARTÃO)";
                }
                if (pernoiteOut.getTipoPagamento().equals(TipoPagamento.DINHEIRO)) {
                    relatorio += "(DINHEIRO)";
                }
                mapaGeral.setApartment(pernoiteOut.getApartamento().getNumero());
                mapaGeral.setData(currentDate);
                mapaGeral.setEntrada(pernoiteOut.getTotal());
                mapaGeral.setReport(relatorio);
                mapaGeral.setSaida(0F);
                mapaGeral.setHora(LocalTime.now());
                mapaGeralService.createMapa(mapaGeral);
            }
        }
    }
}