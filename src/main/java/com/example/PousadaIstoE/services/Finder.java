package com.example.PousadaIstoE.services;

import com.example.PousadaIstoE.exceptions.EntityNotFound;
import com.example.PousadaIstoE.model.*;
import com.example.PousadaIstoE.repository.*;
import org.springframework.stereotype.Service;

@Service
public class Finder {
    private final CashRegisterRepository cashRegisterRepository;
    private final ClientRepository clientRepository;
    private final EmployeeRepository employeeRepository;
    private final RoomRepository roomRepository;
    private final OvernightStayRepository overnightStayRepository;
    private final OvernightStayReservationRepository reservationRepository;
    private final EntryRepository entryRepository;
    private final ItemRepository itemRepository;

    public Finder(
            EntryRepository entryRepository,
            CashRegisterRepository cashRegisterRepository,
            ClientRepository clientRepository,
            EmployeeRepository employeeRepository,
            RoomRepository roomRepository,
            OvernightStayRepository overnightStayRepository,
            OvernightStayReservationRepository reservationRepository,
            EntryRepository entryRepository1, ItemRepository itemRepository){
        this.cashRegisterRepository = cashRegisterRepository;
        this.clientRepository = clientRepository;
        this.employeeRepository = employeeRepository;
        this.roomRepository = roomRepository;
        this.overnightStayRepository = overnightStayRepository;
        this.reservationRepository = reservationRepository;
        this.entryRepository = entryRepository1;
        this.itemRepository = itemRepository;
    }

    public Client clientById(Long client_id){
       return clientRepository.findById(client_id)
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

    public OvernightStay overnightStayById(Long overNightStay_id){
        return overnightStayRepository.findById(overNightStay_id)
                .orElseThrow(()-> new EntityNotFound("OverNight Stay not found"));
    }

    public OvernightStayReservation reservationById(Long reservation_id){
        return reservationRepository.findById(reservation_id)
                .orElseThrow(()-> new EntityNotFound("Reservation not found"));
    }

    public Entry entryById(Long entry_id){
        return entryRepository.findById(entry_id)
                .orElseThrow(()-> new EntityNotFound("Entry not found"));
    }

    public Item itemById(Long item_id){
        return itemRepository.findById(item_id)
                .orElseThrow(()-> new EntityNotFound("Item not found"));
    }

    public String replace(String string) {
        String regex = "[^a-zA-Z0-9\\s]";
        String newString = string.replaceAll(regex, "");
        return newString.toUpperCase();
    }
}
