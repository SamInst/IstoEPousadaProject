package com.example.PousadaIstoE.services;

import com.example.PousadaIstoE.model.CalculatePaymentType;
import com.example.PousadaIstoE.model.PaymentType;
import com.example.PousadaIstoE.repository.CalculatePaymentTypeRepository;
import com.example.PousadaIstoE.request.CalculatePaymentTypeRequest;
import org.springframework.stereotype.Service;

@Service
public class CalculatePaymentTypeService {

    private final CalculatePaymentTypeRepository calculatePaymentTypeRepository;

    public CalculatePaymentTypeService(CalculatePaymentTypeRepository calculatePaymentTypeRepository) {
        this.calculatePaymentTypeRepository = calculatePaymentTypeRepository;
    }


}
