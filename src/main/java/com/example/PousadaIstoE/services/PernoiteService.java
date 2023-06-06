package com.example.PousadaIstoE.services;

import com.example.PousadaIstoE.exceptions.EntityConflict;
import com.example.PousadaIstoE.exceptions.EntityDates;
import com.example.PousadaIstoE.exceptions.EntityNotFound;
import com.example.PousadaIstoE.model.MapaGeral;
import com.example.PousadaIstoE.model.Pernoites;
import com.example.PousadaIstoE.model.Quartos;
import com.example.PousadaIstoE.repository.MapaGeralRepository;
import com.example.PousadaIstoE.repository.PernoitesRepository;
import com.example.PousadaIstoE.repository.QuartosRepository;
import com.example.PousadaIstoE.response.PernoiteResponse;
import com.example.PousadaIstoE.response.StatusDoQuarto;
import com.example.PousadaIstoE.response.StatusPagamento;
import com.example.PousadaIstoE.response.TipoPagamento;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.BeanUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Period;
import java.util.List;

@Service
public class PernoiteService {
    Float price;
    @PersistenceContext
    private EntityManager manager;
    private final PernoitesRepository pernoitesRepository;
    private final QuartosRepository quartosRepository;
    private final MapaGeralRepository mapaGeralRepository;

    protected PernoiteService(PernoitesRepository pernoitesRepository, QuartosRepository quartosRepository, MapaGeralRepository mapaGeralRepository) {
        this.pernoitesRepository = pernoitesRepository;
        this.quartosRepository = quartosRepository;
        this.mapaGeralRepository = mapaGeralRepository;
    }

    public List<Pernoites> findAll() {
        return pernoitesRepository.findAll();
    }

    public ResponseEntity<PernoiteResponse> findById(Long id) {
        final var pernoites = pernoitesRepository.findById(id).orElseThrow(() -> new EntityNotFound("Pernoite não encontrado"));
        quantidadeDePessoas2(pernoites);

        Integer p1 = Period.between(pernoites.getDataEntrada(), pernoites.getDataSaida()).getDays();
        Float valor_total = pernoites.getTotal() * p1;
        final var response = new PernoiteResponse(
                new PernoiteResponse.Client(
                        pernoites.getClient().getName(),
                        pernoites.getClient().getPhone()
                ),
                pernoites.getApartamento().getNumero(),
                pernoites.getDataEntrada(),
                pernoites.getDataSaida(),
                pernoites.getPernoiteConsumo(),
                new PernoiteResponse.Valores(
                        pernoites.getQuantidadePessoa(),
                        p1,
                        pernoites.getTotal(),
                        valor_total,
                        pernoites.getTipoPagamento(),
                        pernoites.getStatus_pagamento()
                )
        );
        return ResponseEntity.ok(response);
    }

    public Pernoites createPernoite(Pernoites pernoites) {
        if (pernoites.getClient() == null){
            throw new EntityConflict("É preciso informar o hóspede.");
        }
        quantidadeDePessoas2(pernoites);
        validacaoDeApartamento(pernoites);
        if (pernoites.getStatus_pagamento() == null) {
            pernoites.setStatus_pagamento(StatusPagamento.PENDENTE);
        }
        if (pernoites.getTipoPagamento() == null) {
            pernoites.setTipoPagamento(TipoPagamento.PENDENTE);
        }
        return pernoitesRepository.save(pernoites);
    }

    public Pernoites updatePernoiteData(Long pernoiteId, Pernoites pernoites) {
        Pernoites pernoites1 = pernoitesRepository.findById(pernoiteId).get();
        BeanUtils.copyProperties(pernoites1, pernoites, "id");

        return pernoitesRepository.save(pernoites1);
    }

//    private void quantidadeDePessoas(Pernoites pernoites) {
//        Integer quantidadePessoa = pernoites.getQuantidadePessoa();
//        switch (quantidadePessoa) {
//            case 1 -> price = 90F;
//            case 2 -> price = 130F;
//            case 3 -> price = 180F;
//            case 4 -> price = 220F;
//            case 5 -> price = 280F;
//            default -> throw new EntityConflict("Não podem ser inseridos mais de 5 pessoas no mesmo quarto");
//        }
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
        if (pernoite.getDataEntrada().isBefore(LocalDate.now())
                || pernoite.getDataSaida().isBefore(LocalDate.now())) {
            throw new EntityDates("A data inserida não pode ser inferior a hoje");
        }
        if (pernoite.getApartamento().getStatusDoQuarto().equals(StatusDoQuarto.OCUPADO)
                && pernoite.getDataEntrada().equals(LocalDate.now())
        ) {
            throw new EntityConflict("O apartamento já está ocupado.");
        }
        if (pernoite.getApartamento().getStatusDoQuarto().equals(StatusDoQuarto.NECESSITA_LIMPEZA)) {
            throw new EntityConflict("O apartamento necessita de limpeza.");
        }
        for (Pernoites pernoiteCadastrado : pernoitesCadastrados) {
            if (pernoite.getDataEntrada().isBefore(pernoiteCadastrado.getDataSaida())
                    && pernoite.getDataSaida().isAfter(pernoiteCadastrado.getDataEntrada())) {
                throw new EntityConflict("O apartamento já está ocupado entre as datas informadas.");
            }
            if (pernoite.getDataSaida().isBefore(pernoite.getDataEntrada())) {
                throw new EntityDates("A data de Saída não pode ser inferior a data de entrada");
            }
            if (pernoite.getDataEntrada().equals(pernoite.getDataSaida())
                    || pernoite.getDataSaida().equals(pernoite.getDataEntrada())) {
                throw new EntityDates("A data de Entrada/Saída não podem ser iguais");
            }
        }
        pernoitesRepository.save(pernoite);
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

        Float totalMapaGeral = manager.createQuery("SELECT m.total FROM MapaGeral m ORDER BY m.id DESC", Float.class)
                .setMaxResults(1)
                .getSingleResult();

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
                mapaGeralRepository.save(mapaGeral);
            }
        }
    }
}