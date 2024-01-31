package com.example.PousadaIstoE.services;

import com.example.PousadaIstoE.builders.CashRegisterBuilder;
import com.example.PousadaIstoE.model.CashRegister;
import com.example.PousadaIstoE.repository.CashRegisterRepository;
import com.example.PousadaIstoE.request.CashRegisterRequest;
import com.example.PousadaIstoE.response.CashRegisterResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CashRegisterService {
    private final CashRegisterRepository cashRegisterRepository;
    public CashRegisterService(CashRegisterRepository cashRegisterRepository) {
        this.cashRegisterRepository = cashRegisterRepository;
    }
    public Page<CashRegisterResponse> listAllMaps(Pageable pageable){
        var allRegisters = cashRegisterRepository.findAll(pageable);
        List<CashRegisterResponse> cashRegisterResponseList = new ArrayList<>();
        allRegisters.forEach( map -> {
            CashRegisterResponse cashRegisterResponse = new CashRegisterResponse(
                    map.getId(),
                    map.getDate(),
                    map.getHour(),
                    map.getReport(),
                    map.getApartment(),
                    map.getCashIn(),
                    map.getCashOut(),
                    map.getTotal()
                    );
            cashRegisterResponseList.add(cashRegisterResponse);
        });
        allRegisters.stream()
                .sorted(Comparator.comparingLong(CashRegister::getId)
                        .reversed())
                .collect(Collectors.toList());
        return new PageImpl<>(cashRegisterResponseList, pageable, allRegisters.getTotalElements());
    }

    public void createMapa(CashRegisterRequest request) {
        Float lastTotal = cashRegisterRepository.findLastTotal() != null ? cashRegisterRepository.findLastTotal() : 0F;
        var total = lastTotal + request.cashIn() - request.cashOut();

        var cashRegister = new CashRegisterBuilder()
                .hour(LocalTime.now())
                .date(LocalDate.now())
                .cashIn(request.cashIn())
                .report(request.report().toUpperCase())
                .cashOut(request.cashOut())
                .apartment(request.apartment())
                .total(total)
                .build();
        cashRegisterRepository.save(cashRegister);
    }

    public List<CashRegister> findByDate(LocalDate date){
      return cashRegisterRepository.findByDate(date);
    }

}