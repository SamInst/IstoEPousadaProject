package com.example.PousadaIstoE.services;

import com.example.PousadaIstoE.exceptions.EntityNotFound;
import com.example.PousadaIstoE.model.Client;
import com.example.PousadaIstoE.model.Employee;
import com.example.PousadaIstoE.model.Rooms;
import com.example.PousadaIstoE.repository.CashRegisterRepository;
import com.example.PousadaIstoE.repository.ClientRepository;
import com.example.PousadaIstoE.repository.EmployeeRepository;
import com.example.PousadaIstoE.repository.RoomRepository;
import org.springframework.stereotype.Service;

@Service
public class Finder {
    private final CashRegisterRepository cashRegisterRepository;
    private final ClientRepository clientRepository;
    private final EmployeeRepository employeeRepository;
    private final RoomRepository roomRepository;

    public Finder(CashRegisterRepository cashRegisterRepository, ClientRepository clientRepository, EmployeeRepository employeeRepository, RoomRepository roomRepository) {
        this.cashRegisterRepository = cashRegisterRepository;
        this.clientRepository = clientRepository;
        this.employeeRepository = employeeRepository;
        this.roomRepository = roomRepository;
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


}
