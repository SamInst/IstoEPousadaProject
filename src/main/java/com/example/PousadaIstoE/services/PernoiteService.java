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
                    pernoite.getRoom().getNumber(),
                    pernoite.getStartDate(),
                    pernoite.getEndDate(),
                    pernoite.getAmountPeople()
            );
            pernoiteShortResponseList.add(pernoiteShortResponse);
        });
        return pernoiteShortResponseList;
    }

    public OvernightStayResponse findById(Long id) {
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
        Integer p1 = between(pernoites.getStartDate(), pernoites.getEndDate()).getDays();
        Float total_diarias = pernoites.getTotal() * p1;
        var valor_total = valorTotal(id, pernoites, p1, total_diarias);


        return new OvernightStayResponse(
                pernoites.getId(),
                new OvernightStayResponse.Client(
                        pernoites.getClient().getName(),
                        pernoites.getClient().getCpf(),
                        pernoites.getClient().getPhone()
                ),
                acompanhantePernoiteShortResponseList,
                new OvernightStayResponse.Rooms(pernoites.getRoom().getNumber()),
                pernoites.getStartDate(),
                pernoites.getEndDate(),
                consumo_pernoite,
                new OvernightStayResponse.Valores(
                        pernoites.getAmountPeople(),
                        p1,
                        pernoites.getTotal(),
                        totalConsumo,
                        total_diarias,
                        pernoites.getTipoPagamento(),
                        pernoites.getPaymentStatus(),
                        valor_total
                )
        );
    }

    public Double valorTotal(Long id, OvernightStay overnightStay, Integer p1, Float total_diarias){
        Double totalConsumo = manager.createQuery(
        "SELECT sum(m.total) FROM OverNightStayConsumption m where m.pernoites.id = :id", Double.class)
        .setParameter("id", id)
        .getSingleResult(); if (totalConsumo == null){ totalConsumo = 0D; }
        return total_diarias + totalConsumo;
    }

    public OvernightStay createPernoite(OvernightStay overnightStay) {
        if (overnightStay.getClient() == null){
            throw new EntityConflict("É preciso informar o hóspede.");
        }
        Integer periodoDias = between(overnightStay.getStartDate(), overnightStay.getEndDate()).getDays();

        quantidadeDePessoas2(overnightStay);
        validacaoDeApartamento(overnightStay);
        Float a = overnightStay.getTotal() * periodoDias;
        overnightStay.setTotal(a);
        if (overnightStay.getPaymentStatus() == null) {
            overnightStay.setPaymentStatus(PaymentStatus.PENDENTE);
        }
        if (overnightStay.getTipoPagamento() == null) {
            overnightStay.setTipoPagamento(PaymentType.PENDING);
        }
        return pernoitesRepository.save(overnightStay);
    }

    public OvernightStay updatePernoiteData(Long pernoiteId, OvernightStay request) {
        OvernightStay pernoite = pernoitesRepository.findById(pernoiteId)
                .orElseThrow(()-> new EntityNotFound("Pernoite não encontrado"));

        var pernoiteAtualizado = new OvernightStay(
            pernoite.getId(),
            pernoite.getRoom(),
            pernoite.getClient(),
            pernoite.getStartDate(),
            pernoite.getEndDate(),
            pernoite.getAmountPeople(),
            request.getTipoPagamento(),
            request.getPaymentStatus(),
            pernoite.getTotal()
        );


        return pernoitesRepository.save(pernoiteAtualizado);
    }

    private void quantidadeDePessoas2(OvernightStay overnightStay) {
        Integer quantidadePessoa = overnightStay.getAmountPeople();
        switch (quantidadePessoa) {
            case 1 -> overnightStay.setTotal(90F);
            case 2 -> overnightStay.setTotal(130F);
            case 3 -> overnightStay.setTotal(180F);
            case 4 -> overnightStay.setTotal(220F);
            case 5 -> overnightStay.setTotal(280F);
            default -> throw new EntityConflict("Não podem ser inseridos mais de 5 pessoas no mesmo quarto");
        }
    }

    private void validacaoDeApartamento(OvernightStay pernoite) throws EntityConflict {
        List<OvernightStay> overnightStayCadastrados = pernoitesRepository.findByApartamento_Id(pernoite.getRoom().getId());
        if (pernoite.getStartDate().isBefore(LocalDate.now()) || pernoite.getEndDate().isBefore(LocalDate.now())) {
            throw new EntityDates("A data inserida não pode ser inferior a hoje");
        }
        switch (pernoite.getRoom().getStatusDoQuarto()){
            case OCUPADO    -> throw new EntityConflict("O apartamento já está ocupado.");
            case LIMPEZA    -> throw new EntityConflict("O apartamento necessita de limpeza.");
            case RESERVADO  -> throw new EntityConflict("O apartamento está reservado.");
            case MANUTENCAO -> throw new EntityConflict("O apartamento está em manutenção.");
        }
        for (OvernightStay pernoiteCadastrado : overnightStayCadastrados) {
            if (pernoite.getStartDate().isBefore(pernoiteCadastrado.getEndDate()) && pernoite.getEndDate().isAfter(pernoiteCadastrado.getStartDate())) {
                throw new EntityConflict("O apartamento já está ocupado entre as datas informadas.");
            }
            if (pernoite.getEndDate().isBefore(pernoite.getStartDate())) {
                throw new EntityDates("A data de Saída não pode ser inferior a data de entrada");
            }
            if (pernoite.getStartDate().equals(pernoite.getEndDate()) || pernoite.getEndDate().equals(pernoite.getStartDate())) {
                throw new EntityDates("A data de Entrada/Saída não podem ser iguais");
            }
        }
        pernoitesRepository.save(pernoite);
    }

    public OvernightStayCompanion addAcompanhante(OvernightStayCompanion overnightStayCompanion){
        var b = overnightStayCompanion.getBirth();
        Period age = Period.ofYears(Period.between(LocalDate.now(), b).getYears());
        overnightStayCompanion.setAge(age.getYears());
        return acompanhantePernoiteRepository.save(overnightStayCompanion);
    }

    @Scheduled(cron = "0 * * * * ?") // Tem que executar todos os dias à meia-noite
    public void updateRoomStatus() {
        LocalDate currentDate = LocalDate.now();
        List<OvernightStay> overnightStayToCheckIn = pernoitesRepository.findByDataEntrada(currentDate);

        for (OvernightStay pernoiteIn : overnightStayToCheckIn) {
            Rooms roomsIn = pernoiteIn.getRoom();

            if (pernoiteIn.getStartDate().equals(currentDate)
                    && LocalTime.now().isAfter(LocalTime.of(6,0))
                    && LocalTime.now().isBefore(LocalTime.of(12,0))){
                roomsIn.setStatusDoQuarto(RoomStatus.RESERVED);
                quartosRepository.save(roomsIn);
            }
            if (pernoiteIn.getStartDate().equals(currentDate)
            && LocalTime.now().isAfter(LocalTime.of(12,0))
            ){
                roomsIn.setStatusDoQuarto(RoomStatus.BUSY);
                quartosRepository.save(roomsIn);
            }
        }
    }

    @Scheduled(cron = "0 1 12 * * ?")
    public void updateRoomStatusOut() {
        String relatorio = "Pernoite";
        LocalDate currentDate = LocalDate.now();
        List<OvernightStay> overnightStayToCheckOut = pernoitesRepository.findByDataSaida(currentDate);
        CashRegister cashRegister = new CashRegister();

        for (OvernightStay pernoiteOut : overnightStayToCheckOut) {
            Rooms roomsOut = pernoiteOut.getRoom();
            if (pernoiteOut.getEndDate().equals(currentDate) && LocalTime.now().isAfter(LocalTime.of(12, 0))) {
                roomsOut.setStatusDoQuarto(RoomStatus.DAILY_CLOSED);
                quartosRepository.save(roomsOut);
            }
            if (pernoiteOut.getEndDate().equals(currentDate)
                    && pernoiteOut.getPaymentStatus().equals(PaymentStatus.CONCLUIDO)) {
                if (pernoiteOut.getTipoPagamento().equals(PaymentType.PIX)) {
                    relatorio += " (PIX)";
                }
                if (pernoiteOut.getTipoPagamento().equals(PaymentType.CREDIT_CARD)) {
                    relatorio += " (CARTÃO)";
                }
                if (pernoiteOut.getTipoPagamento().equals(PaymentType.CASH)) {
                    relatorio += "(DINHEIRO)";
                }
                cashRegister.setApartment(pernoiteOut.getRoom().getNumero());
                cashRegister.setData(currentDate);
                cashRegister.setEntrada(pernoiteOut.getTotal());
                cashRegister.setReport(relatorio);
                cashRegister.setSaida(0F);
                cashRegister.setHora(LocalTime.now());
                mapaGeralService.createMapa(cashRegister);
            }
        }
    }
}