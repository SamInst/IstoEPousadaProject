package com.example.PousadaIstoE.services;

import com.example.PousadaIstoE.Enums.PaymentStatus;
import com.example.PousadaIstoE.Enums.PaymentType;
import com.example.PousadaIstoE.Enums.RoomStatus;
import com.example.PousadaIstoE.builders.CashRegisterBuilder;
import com.example.PousadaIstoE.exceptions.EntityConflict;
import com.example.PousadaIstoE.exceptions.EntityDates;
import com.example.PousadaIstoE.exceptions.EntityNotFound;
import com.example.PousadaIstoE.model.*;
import com.example.PousadaIstoE.repository.OvernightStayCompanionRepository;
import com.example.PousadaIstoE.repository.OvernightStayComsuptionRepository;
import com.example.PousadaIstoE.repository.OvernightStayRepository;
import com.example.PousadaIstoE.repository.RoomRepository;
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

import static com.example.PousadaIstoE.Enums.RoomStatus.*;
import static java.time.Period.*;

@Service
public class OvernightStayService {
    private static final Float EMPTY = 0F;
    @PersistenceContext
    private EntityManager manager;
    private final OvernightStayRepository overnightStayRepository;
    private final RoomRepository roomRepository;
    private final CashRegisterService cashRegisterService;
    private final OvernightStayComsuptionRepository overnightStayComsuptionRepository;
    private final OvernightStayCompanionRepository overnightStayCompanionRepository;

    protected OvernightStayService(OvernightStayRepository overnightStayRepository, RoomRepository roomRepository, CashRegisterService cashRegisterService, OvernightStayComsuptionRepository overnightStayComsuptionRepository, OvernightStayCompanionRepository overnightStayCompanionRepository) {
        this.overnightStayRepository = overnightStayRepository;
        this.roomRepository = roomRepository;
        this.cashRegisterService = cashRegisterService;
        this.overnightStayComsuptionRepository = overnightStayComsuptionRepository;
        this.overnightStayCompanionRepository = overnightStayCompanionRepository;
    }

    public List<OvernightStayShortResponse> findAll() {
        var allPernoites =  overnightStayRepository.findAll();
        List<OvernightStayShortResponse> overnightStayShortResponseList = new ArrayList<>();

        allPernoites.forEach(pernoite->{
            OvernightStayShortResponse overnightStayShortResponse = new OvernightStayShortResponse(
                    pernoite.getId(),
                    new OvernightStayShortResponse.Client(pernoite.getClient().getName()),
                    pernoite.getRoom().getNumber(),
                    pernoite.getStartDate(),
                    pernoite.getEndDate(),
                    pernoite.getAmountPeople()
            );
            overnightStayShortResponseList.add(overnightStayShortResponse);
        });
        return overnightStayShortResponseList;
    }

    public OvernightStayResponse findById(Long id) {
        final var pernoites = overnightStayRepository.findById(id).orElseThrow(() -> new EntityNotFound("Pernoite não encontrado"));
        final var consumo_pernoite = overnightStayComsuptionRepository.findPernoiteConsumoByPernoites_Id(id);
        final var totalConsumo = overnightStayComsuptionRepository.findConsumoTotal(id);
        final var acompanhantes = overnightStayCompanionRepository.findAllByPernoites_Id(id);
        amountPeoplePrice(pernoites);

        List<OvernightStayCompanionShortResponse> overnightStayCompanionShortResponseList = new ArrayList<>();
        acompanhantes.forEach(acompanhantePernoite -> {
            OvernightStayCompanionShortResponse overnightStayCompanionShortResponse = new OvernightStayCompanionShortResponse(
                    acompanhantePernoite.getId(),
                    acompanhantePernoite.getName(),
                    acompanhantePernoite.getCpf(),
                    acompanhantePernoite.getAge()
            );
            overnightStayCompanionShortResponseList.add(overnightStayCompanionShortResponse);
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
                overnightStayCompanionShortResponseList,
                new OvernightStayResponse.Rooms(pernoites.getRoom().getNumber()),
                pernoites.getStartDate(),
                pernoites.getEndDate(),
                consumo_pernoite,
                new OvernightStayResponse.Values(
                        pernoites.getAmountPeople(),
                        p1,
                        pernoites.getTotal(),
                        totalConsumo,
                        total_diarias,
                        pernoites.getPaymentType(),
                        pernoites.getPaymentStatus(),
                        valor_total
                )
        );
    }

    public Double valorTotal(Long id, OvernightStay overnightStay, Integer p1, Float total_diarias){
        Double totalConsumo = manager.createQuery(
        "SELECT sum(m.total) FROM OverNightStayConsumption m where m.overnightStay.id = :id", Double.class)
        .setParameter("id", id)
        .getSingleResult(); if (totalConsumo == null){ totalConsumo = 0D; }
        return total_diarias + totalConsumo;
    }

    public OvernightStay createPernoite(OvernightStay overnightStay) {
        if (overnightStay.getClient() == null){
            throw new EntityConflict("É preciso informar o hóspede.");
        }
        Integer periodoDias = between(overnightStay.getStartDate(), overnightStay.getEndDate()).getDays();

        amountPeoplePrice(overnightStay);
        apartmentValidation(overnightStay);
        Float a = overnightStay.getTotal() * periodoDias;
        overnightStay.setTotal(a);
        if (overnightStay.getPaymentStatus() == null) {
            overnightStay.setPaymentStatus(PaymentStatus.PENDENTE);
        }
        if (overnightStay.getPaymentType() == null) {
            overnightStay.setPaymentType(PaymentType.PENDING);
        }
        return overnightStayRepository.save(overnightStay);
    }

    public OvernightStay updatePernoiteData(Long pernoiteId, OvernightStay request) {
        OvernightStay pernoite = overnightStayRepository.findById(pernoiteId)
                .orElseThrow(()-> new EntityNotFound("Pernoite não encontrado"));

        var pernoiteAtualizado = new OvernightStay(
            pernoite.getId(),
            pernoite.getRoom(),
            pernoite.getClient(),
            pernoite.getStartDate(),
            pernoite.getEndDate(),
            pernoite.getAmountPeople(),
            request.getPaymentType(),
            request.getPaymentStatus(),
            pernoite.getTotal()
        );
        return overnightStayRepository.save(pernoiteAtualizado);
    }

    private void amountPeoplePrice(OvernightStay overnightStay) {
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

    private void apartmentValidation(OvernightStay overnight) throws EntityConflict {
        List<OvernightStay> overnightStayCadastrados = overnightStayRepository.findByRoom_Id(overnight.getRoom().getId());
        if (overnight.getStartDate().isBefore(LocalDate.now()) || overnight.getEndDate().isBefore(LocalDate.now())) {
            throw new EntityDates("A data inserida não pode ser inferior a hoje");
        }
        switch (overnight.getRoom().getRoomStatus()){
            case BUSY           -> throw new EntityConflict("O apartment já está ocupado.");
            case NEEDS_CLEANING -> throw new EntityConflict("O apartment necessita de limpeza.");
            case RESERVED       -> throw new EntityConflict("O apartment está reservado.");
            case MAINTENANCE    -> throw new EntityConflict("O apartment está em manutenção.");
        }
        for (OvernightStay pernoiteCadastrado : overnightStayCadastrados) {
            if (overnight.getStartDate().isBefore(pernoiteCadastrado.getEndDate()) && overnight.getEndDate().isAfter(pernoiteCadastrado.getStartDate())) {
                throw new EntityConflict("O apartment já está ocupado entre as datas informadas.");
            }
            if (overnight.getEndDate().isBefore(overnight.getStartDate())) {
                throw new EntityDates("A data de Saída não pode ser inferior a data de entrada");
            }
            if (overnight.getStartDate().equals(overnight.getEndDate()) || overnight.getEndDate().equals(overnight.getStartDate())) {
                throw new EntityDates("A data de Entrada/Saída não podem ser iguais");
            }
        }
        overnightStayRepository.save(overnight);
    }

    public OvernightStayCompanion addCompanion(OvernightStayCompanion overnightStayCompanion){
        var birth = overnightStayCompanion.getBirth();
        Period age = Period.ofYears(Period.between(LocalDate.now(), birth).getYears());
        overnightStayCompanion.setAge(age.getYears());
        return overnightStayCompanionRepository.save(overnightStayCompanion);
    }

    @Scheduled(cron = "0 * * * * ?") // Needs to be executed at twelve o'clock (midnight)
    public void updateRoomStatus() {
        LocalDate currentDate = LocalDate.now();
        List<OvernightStay> overnightStayToCheckIn = overnightStayRepository.findByStartDate(currentDate);

        for (OvernightStay pernoiteIn : overnightStayToCheckIn) {
            Rooms roomsIn = pernoiteIn.getRoom();

            if (pernoiteIn.getStartDate().equals(currentDate)
                    && LocalTime.now().isAfter(LocalTime.of(6,0))
                    && LocalTime.now().isBefore(LocalTime.of(12,0))){
                roomsIn.setRoomStatus(RESERVED);
                roomRepository.save(roomsIn);
            }
            if (pernoiteIn.getStartDate().equals(currentDate)
            && LocalTime.now().isAfter(LocalTime.of(12,0))
            ){
                roomsIn.setRoomStatus(BUSY);
                roomRepository.save(roomsIn);
            }
        }
    }

    @Scheduled(cron = "0 1 12 * * ?")
    public void updateRoomStatusOut() {
        String report = "Pernoite";
        LocalDate currentDate = LocalDate.now();
        List<OvernightStay> overnightStayToCheckOut = overnightStayRepository.findByEndDate(currentDate);

        for (OvernightStay overnightOut : overnightStayToCheckOut) {
            Rooms roomsOut = overnightOut.getRoom();
            if (overnightOut.getEndDate().equals(currentDate) && LocalTime.now().isAfter(LocalTime.of(12, 0))) {
                roomsOut.setRoomStatus(DAILY_CLOSED);
                roomRepository.save(roomsOut);
            }
            if (overnightOut.getEndDate().equals(currentDate)
                    && overnightOut.getPaymentStatus().equals(PaymentStatus.CONCLUIDO)) {

                switch (overnightOut.getPaymentType()){
                    case PIX -> report += " (PIX)";
                    case CREDIT_CARD ->  report += " (CARTÃO)";
                    case CASH ->  report += "(DINHEIRO)";
                }
                CashRegister cashRegister = new CashRegisterBuilder()
                        .apartment(overnightOut.getRoom().getNumber())
                        .data(currentDate)
                        .cashIn(overnightOut.getTotal())
                        .report(report)
                        .cashOut(EMPTY)
                        .hour(LocalTime.now())
                        .build();

                cashRegisterService.createMapa(cashRegister);
            }
        }
    }
}