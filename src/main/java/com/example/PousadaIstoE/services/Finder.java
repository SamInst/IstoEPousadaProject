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
    private final OvernightStayCompanionRepository overnightStayCompanionRepository;
    private final OvernightStayRepository overnightStayRepository;
    private final OvernightStayReservationRepository reservationRepository;
    private final OvernightStayCompanionRepository companionRepository;

    public Finder(
            CashRegisterRepository cashRegisterRepository,
            ClientRepository clientRepository,
            EmployeeRepository employeeRepository,
            RoomRepository roomRepository,
            OvernightStayCompanionRepository overnightStayCompanionRepository,
            OvernightStayRepository overnightStayRepository, OvernightStayReservationRepository reservationRepository, OvernightStayCompanionRepository companionRepository) {
        this.cashRegisterRepository = cashRegisterRepository;
        this.clientRepository = clientRepository;
        this.employeeRepository = employeeRepository;
        this.roomRepository = roomRepository;
        this.overnightStayCompanionRepository = overnightStayCompanionRepository;
        this.overnightStayRepository = overnightStayRepository;
        this.reservationRepository = reservationRepository;
        this.companionRepository = companionRepository;
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
    public OvernightStayCompanion companionById(Long companion_id){
        return overnightStayCompanionRepository.findById(companion_id)
                .orElseThrow(()-> new EntityNotFound("Companion not found"));
    }


}
