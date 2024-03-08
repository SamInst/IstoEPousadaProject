package com.example.PousadaIstoE.services;

import com.example.PousadaIstoE.exceptions.EntityNotFound;
import com.example.PousadaIstoE.model.*;
import com.example.PousadaIstoE.repository.CountryRepository;
import com.example.PousadaIstoE.repository.*;
import org.springframework.stereotype.Service;

import java.text.Normalizer;

@Service
public class Finder {
    private final CashRegisterRepository cashRegisterRepository;
    private final CustomerRepository customerRepository;
    private final EmployeeRepository employeeRepository;
    private final RoomRepository roomRepository;
    private final OvernightStayRepository overnightStayRepository;
    private final OvernightStayReservationRepository reservationRepository;
    private final EntryRepository entryRepository;
    private final ItemRepository itemRepository;
    private final CountryRepository countryRepository;
    private final CountyRepository countyRepository;
    private final StatesRepository statesRepository;
    private final PaymentTypeRepository paymentTypeRepository;
    private final CalculatePaymentTypeOvernightRepository calculatePaymentTypeOvernightRepository;
    private final CalculatePaymentTypeEntryRepository calculatePaymentTypeEntryRepository;
    private final CalculatePaymentTypeReservationRepository calculatePaymentTypeReservationRepository;

    public Finder(
            EntryRepository entryRepository,
            CashRegisterRepository cashRegisterRepository,
            CustomerRepository customerRepository,
            EmployeeRepository employeeRepository,
            RoomRepository roomRepository,
            OvernightStayRepository overnightStayRepository,
            OvernightStayReservationRepository reservationRepository,
            EntryRepository entryRepository1, ItemRepository itemRepository,
            CountryRepository countryRepository,
            CountyRepository countyRepository,
            StatesRepository statesRepository,
            PaymentTypeRepository paymentTypeRepository,
            CalculatePaymentTypeOvernightRepository calculatePaymentTypeOvernightRepository,
            CalculatePaymentTypeEntryRepository calculatePaymentTypeEntryRepository,
            CalculatePaymentTypeReservationRepository calculatePaymentTypeReservationRepository){
        this.cashRegisterRepository = cashRegisterRepository;
        this.customerRepository = customerRepository;
        this.employeeRepository = employeeRepository;
        this.roomRepository = roomRepository;
        this.overnightStayRepository = overnightStayRepository;
        this.reservationRepository = reservationRepository;
        this.entryRepository = entryRepository1;
        this.itemRepository = itemRepository;
        this.countryRepository = countryRepository;
        this.countyRepository = countyRepository;
        this.statesRepository = statesRepository;
        this.paymentTypeRepository = paymentTypeRepository;
        this.calculatePaymentTypeOvernightRepository = calculatePaymentTypeOvernightRepository;
        this.calculatePaymentTypeEntryRepository = calculatePaymentTypeEntryRepository;
        this.calculatePaymentTypeReservationRepository = calculatePaymentTypeReservationRepository;
    }

    public Customer clientById(Long client_id){
       return customerRepository.findById(client_id)
               .orElseThrow(()-> new EntityNotFound("Client not found."));
    }

    public Employee employeeById(Long employee_id){
        return employeeRepository.findById(employee_id)
                .orElseThrow(()-> new EntityNotFound("Employee not Found"));
    }
    public Rooms roomById(Long room_id){
        return roomRepository.findById(room_id)
                .orElseThrow(()-> new EntityNotFound("Room not Found"));
    }
    public Rooms roomByNumber(Integer room_number){
        return roomRepository.findRoomsByNumber(room_number);
    }

    public OvernightStay overnightStayById(Long overNightStay_id){
        return overnightStayRepository.findById(overNightStay_id)
                .orElseThrow(()-> new EntityNotFound("OverNight Stay not found"));
    }

    public Reservation reservationById(Long reservation_id){
        return reservationRepository.findOvernightStayReservationById(reservation_id);
    }

    public Entry entryById(Long entry_id){
        return entryRepository.findById(entry_id)
                .orElseThrow(()-> new EntityNotFound("Entry not found"));
    }

    public Item itemById(Long item_id){
        return itemRepository.findById(item_id)
                .orElseThrow(()-> new EntityNotFound("Item not found"));
    }
    public Country countryById(Long country_id){
        return countryRepository.findById(country_id)
                .orElseThrow(()-> new EntityNotFound("Country not found"));
    }

    public County countyById(Long county_id){
        return countyRepository.findById(county_id)
                .orElseThrow(()-> new EntityNotFound("County not found"));
    }

    public States stateById(Long state_id){
        return statesRepository.findById(state_id)
                .orElseThrow(()-> new EntityNotFound("State not found"));
    }

    public PaymentType paymentById(Long payment_id){
       return paymentTypeRepository.findById(payment_id)
               .orElseThrow(()-> new EntityNotFound("Payment Not Found"));
    }

    public PaymentOvernight calculatePaymentOvernightById(Long payment_id){
        return calculatePaymentTypeOvernightRepository.findById(payment_id)
                .orElseThrow(()-> new EntityNotFound("CalculatePayment not found"));
    }

    public PaymentEntry calculatePaymentEntryById(Long payment_id){
        return calculatePaymentTypeEntryRepository.findById(payment_id)
                .orElseThrow(()-> new EntityNotFound("CalculatePayment not found"));
    }

    public PaymentReservation calculatePaymentReservationById(Long reservation_id){
        return calculatePaymentTypeReservationRepository.findById(reservation_id)
                .orElseThrow(()-> new EntityNotFound("Reservation not found"));
    }

    public String replace(String string) {
        String stringSemAcentos = Normalizer.normalize(string, Normalizer.Form.NFD)
                .replaceAll("[^\\p{ASCII}]", "");
        return stringSemAcentos.replaceAll("[^a-zA-Z0-9\\s]", "").toUpperCase();
    }
}
