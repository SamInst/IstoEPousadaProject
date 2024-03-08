package com.example.PousadaIstoE.services;

import com.example.PousadaIstoE.model.PaymentEntry;
import com.example.PousadaIstoE.model.PaymentOvernight;
import com.example.PousadaIstoE.model.PaymentReservation;
import com.example.PousadaIstoE.repository.CalculatePaymentTypeEntryRepository;
import com.example.PousadaIstoE.repository.CalculatePaymentTypeOvernightRepository;
import com.example.PousadaIstoE.repository.CalculatePaymentTypeReservationRepository;
import com.example.PousadaIstoE.request.CalculatePaymentTypeRequest;
import com.example.PousadaIstoE.response.PaymentResponse;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class PaymentService {
    private final CalculatePaymentTypeOvernightRepository calculatePaymentTypeOvernightRepository;
    private final CalculatePaymentTypeEntryRepository calculatePaymentTypeEntryRepository;
    private final CalculatePaymentTypeReservationRepository calculatePaymentTypeReservationRepository;
    private final Finder find;

    public PaymentService(CalculatePaymentTypeOvernightRepository calculatePaymentTypeOvernightRepository, CalculatePaymentTypeEntryRepository calculatePaymentTypeEntryRepository, CalculatePaymentTypeReservationRepository calculatePaymentTypeReservationRepository, Finder find) {
        this.calculatePaymentTypeOvernightRepository = calculatePaymentTypeOvernightRepository;
        this.calculatePaymentTypeEntryRepository = calculatePaymentTypeEntryRepository;
        this.calculatePaymentTypeReservationRepository = calculatePaymentTypeReservationRepository;
        this.find = find;
    }

    public void addCalculatePaymentTypeOvernight(Long overnight_id, List<CalculatePaymentTypeRequest> request){
        var overnight = find.overnightStayById(overnight_id);
        List<PaymentOvernight> paymentOvernightList = new ArrayList<>();

        request.forEach(newRequest -> {
            var payment = find.paymentById(newRequest.payment_type_id());

                PaymentOvernight paymentOvernight = new PaymentOvernight();
                paymentOvernight.setPaymentType(payment);
                paymentOvernight.setOvernightStay(overnight);
                paymentOvernight.setValue(newRequest.value());
                paymentOvernight.setPaymentStatus(newRequest.status());
                paymentOvernightList.add(paymentOvernight);
        });
        calculatePaymentTypeOvernightRepository.saveAll(paymentOvernightList);
    }

    public void changeCalculatePaymentTypeOvernight(Long calculate_type, CalculatePaymentTypeRequest request){

        var payment = find.paymentById(request.payment_type_id());
        var calculatePaymentTypeOvernight = find.calculatePaymentOvernightById(calculate_type);
        calculatePaymentTypeOvernight.setPaymentType(payment);
        calculatePaymentTypeOvernight.setValue(request.value());
        calculatePaymentTypeOvernight.setPaymentStatus(request.status());
        calculatePaymentTypeOvernightRepository.save(calculatePaymentTypeOvernight);
    }

    public void removeCalculatePaymentTypeOvernight(Long calculate_type){
        var calculatePaymentTypeOvernight = find.calculatePaymentOvernightById(calculate_type);
        calculatePaymentTypeOvernightRepository.deleteById(calculatePaymentTypeOvernight.getId());
    }

    public void addCalculatePaymentTypeEntry(Long entry_id, List<CalculatePaymentTypeRequest> request){
        List<PaymentEntry> paymentEntryList = new ArrayList<>();
        var allPaymentTypes = calculatePaymentTypeEntryRepository.findAllByEntry_Id(entry_id);
        request.forEach(newRequest -> {
            var payment = find.paymentById(newRequest.payment_type_id());
            var entry = find.entryById(entry_id);
            if (allPaymentTypes.stream()
                    .noneMatch(paymentType -> paymentType.getPaymentType().equals(payment))){
                PaymentEntry paymentEntry = new PaymentEntry();
                paymentEntry.setPaymentType(payment);
                paymentEntry.setEntry(entry);
                paymentEntry.setValue(newRequest.value());
                paymentEntry.setPaymentStatus(newRequest.status());
                paymentEntryList.add(paymentEntry);
            }
        });
        calculatePaymentTypeEntryRepository.saveAll(paymentEntryList);
    }

    public void changeCalculatePaymentTypeEntry(Long calculate_type, CalculatePaymentTypeRequest request){
        var calculatePaymentTypeEntry = find.calculatePaymentEntryById(calculate_type);
        var paymentType = find.paymentById(request.payment_type_id());
        calculatePaymentTypeEntry.setPaymentType(paymentType);
        calculatePaymentTypeEntry.setValue(request.value());
        calculatePaymentTypeEntry.setPaymentStatus(request.status());
        calculatePaymentTypeEntryRepository.save(calculatePaymentTypeEntry);
    }

    public void removeCalculatePaymentTypeEntry(Long calculate_type){
        var calculatePaymentTypeEntry = find.calculatePaymentEntryById(calculate_type);
        calculatePaymentTypeEntryRepository.deleteById(calculatePaymentTypeEntry.getId());
    }

    public void addCalculatePaymentTypeReservation(Long reservation_id, List<CalculatePaymentTypeRequest> request){
        var reservation = find.reservationById(reservation_id);

        List<PaymentReservation> calculatePaymentTypeOvernightList = new ArrayList<>();

        request.forEach(newRequest -> {
            var payment = find.paymentById(newRequest.payment_type_id());

            PaymentReservation paymentReservation = new PaymentReservation();
            paymentReservation.setPaymentType(payment);
            paymentReservation.setReservation(reservation);
            paymentReservation.setValue(newRequest.value());
            paymentReservation.setPaymentStatus(newRequest.status());
            calculatePaymentTypeOvernightList.add(paymentReservation);
        });
        calculatePaymentTypeReservationRepository.saveAll(calculatePaymentTypeOvernightList);
    }

    public void changeCalculatePaymentTypeReservation(Long calculate_type, CalculatePaymentTypeRequest request){
        var calculatePaymentTypeReservation = find.calculatePaymentReservationById(calculate_type);
        var paymentType = find.paymentById(request.payment_type_id());
        calculatePaymentTypeReservation.setPaymentType(paymentType);
        calculatePaymentTypeReservation.setValue(request.value());
        calculatePaymentTypeReservation.setPaymentStatus(request.status());
        calculatePaymentTypeReservationRepository.save(calculatePaymentTypeReservation);
    }

    public void removeCalculatePaymentTypeReservation(Long calculate_type){
        var calculatePaymentTypeReservation = find.calculatePaymentReservationById(calculate_type);
        calculatePaymentTypeReservationRepository.deleteById(calculatePaymentTypeReservation.getId());
    }

    public List<PaymentOvernight> findAllByOvernightId(Long overnight_id){
        return calculatePaymentTypeOvernightRepository.findAllByOvernightStay_Id(overnight_id);
    }

    public List<PaymentEntry> findAllByEntryId(Long entry_id){
        return calculatePaymentTypeEntryRepository.findAllByEntry_Id(entry_id);
    }

    public List<PaymentReservation> findAllByReservationId(Long resevation_id){
        return calculatePaymentTypeReservationRepository.findAllByReservation_Id(resevation_id);
    }

    public void savePaymentReservation(PaymentReservation paymentReservation){
        calculatePaymentTypeReservationRepository.save(paymentReservation);
    }

    public Float sumAllValuesEntry(Long entry_id){
        return calculatePaymentTypeEntryRepository.sumAllValues(entry_id);
    }

    public Float sumAllValuesOvernight(Long overnight_id){
        return calculatePaymentTypeOvernightRepository.sumAllValues(overnight_id);
    }

    public static PaymentResponse paymentReservationResponse(PaymentReservation paymentReservation){
        return new PaymentResponse(
                paymentReservation.getId(),
                paymentReservation.getPaymentType().getDescription(),
                paymentReservation.getValue(),
                paymentReservation.getPaymentStatus()
        );
    }

    public static PaymentResponse paymentOvernightResponse(PaymentOvernight paymentOvernight){
        return new PaymentResponse(
                paymentOvernight.getId(),
                paymentOvernight.getPaymentType().getDescription(),
                paymentOvernight.getValue(),
                paymentOvernight.getPaymentStatus()
        );
    }

    public PaymentResponse paymentEntryResponse(PaymentEntry paymentEntry){
        return new PaymentResponse(
                paymentEntry.getId(),
                paymentEntry.getPaymentType().getDescription(),
                paymentEntry.getValue(),
                paymentEntry.getPaymentStatus()
        );
    }




}
