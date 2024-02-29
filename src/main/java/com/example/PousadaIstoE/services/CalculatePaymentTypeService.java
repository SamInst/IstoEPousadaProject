package com.example.PousadaIstoE.services;

import com.example.PousadaIstoE.repository.CalculatePaymentTypeRepository;
import org.springframework.stereotype.Service;

@Service
public class CalculatePaymentTypeService {

    private final CalculatePaymentTypeRepository calculatePaymentTypeRepository;

    public CalculatePaymentTypeService(CalculatePaymentTypeRepository calculatePaymentTypeRepository) {
        this.calculatePaymentTypeRepository = calculatePaymentTypeRepository;
    }


}
