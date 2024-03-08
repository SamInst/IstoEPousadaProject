package com.example.PousadaIstoE.controllers;

import com.example.PousadaIstoE.request.PaymentRequest;
import com.example.PousadaIstoE.services.PaymentService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("/payment-type")
public class PaymentTypeController {
    private final PaymentService paymentService;

    public PaymentTypeController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/overnight/add/{overnight_id}")
    @ResponseStatus(CREATED)
    public void addCalculatePaymentTypeOvernight(
            @PathVariable Long overnight_id,
            @RequestBody List<PaymentRequest> request){
        paymentService.addCalculatePaymentTypeOvernight(overnight_id, request);
    }

    @PutMapping("/overnight/change/{calculate_type}")
    @ResponseStatus(ACCEPTED)
    public void changeCalculatePaymentTypeOvernight(
            @PathVariable Long calculate_type,
            @RequestBody PaymentRequest request){
        paymentService.changeCalculatePaymentTypeOvernight(calculate_type, request);
    }

    @DeleteMapping("/overnight/remove/{calculate_type_overnight}")
    @ResponseStatus(NO_CONTENT)
    public void removeCalculatePaymentTypeOvernight(@PathVariable Long calculate_type_overnight){
        paymentService.removeCalculatePaymentTypeOvernight(calculate_type_overnight);
    }

    @PostMapping("/entry/add/{entry_id}")
    @ResponseStatus(CREATED)
    public void addCalculatePaymentTypeEntry(
            @PathVariable Long entry_id,
            @RequestBody List<PaymentRequest> request){
        paymentService.addCalculatePaymentTypeEntry(entry_id, request);
    }

    @PutMapping("/entry/change/{calculate_type}")
    @ResponseStatus(ACCEPTED)
    public void changeCalculatePaymentTypeEntry(
            @PathVariable Long calculate_type,
            @RequestBody PaymentRequest request){
        paymentService.changeCalculatePaymentTypeEntry(calculate_type, request);
    }

    @DeleteMapping("/entry/remove/{calculate_type_entry}")
    @ResponseStatus(NO_CONTENT)
    public void removeCalculatePaymentTypeEntry(@PathVariable Long calculate_type_entry){
        paymentService.removeCalculatePaymentTypeEntry(calculate_type_entry);
    }

    @PostMapping("/reservation/add/{reservation_id}")
    @ResponseStatus(CREATED)
    public void addCalculatePaymentTypeReservation(
            @PathVariable Long reservation_id,
            @RequestBody List<PaymentRequest> request){
        paymentService.addCalculatePaymentTypeReservation(reservation_id, request);
    }

    @PutMapping("/reservation/change/{calculate_type}")
    @ResponseStatus(ACCEPTED)
    public void changeCalculatePaymentTypeReservation(
            @PathVariable Long calculate_type,
            @RequestBody PaymentRequest request){
        paymentService.changeCalculatePaymentTypeReservation(calculate_type, request);
    }

    @DeleteMapping("/reservation/remove/{calculate_type_reservation}")
    @ResponseStatus(NO_CONTENT)
    public void removeCalculatePaymentTypeReservation(@PathVariable Long calculate_type_reservation){
        paymentService.removeCalculatePaymentTypeReservation(calculate_type_reservation);
    }
}
