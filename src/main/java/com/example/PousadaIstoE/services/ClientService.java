package com.example.PousadaIstoE.services;

import com.example.PousadaIstoE.builders.ClientBuilder;
import com.example.PousadaIstoE.exceptions.EntityInUse;
import com.example.PousadaIstoE.exceptions.EntityNotFound;
import com.example.PousadaIstoE.model.Client;
import com.example.PousadaIstoE.repository.ClientRepository;
import com.example.PousadaIstoE.repository.EmployeeRepository;
import com.example.PousadaIstoE.request.ClientRequest;
import com.example.PousadaIstoE.response.ClientResponse;
import org.springframework.beans.BeanUtils;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClientService {
    private final ClientRepository clientRepository;
    private final EmployeeRepository employeeRepository;

    public ClientService(ClientRepository clientRepository, EmployeeRepository employeeRepository) {
        this.clientRepository = clientRepository;
        this.employeeRepository = employeeRepository;
    }

    public List<Client> findAll(){
        return clientRepository.findAll();
    }
    public ResponseEntity<ClientResponse> findClientById(Long id){
        final var client = clientRepository.findById(id).orElseThrow(() -> new EntityNotFound("Client not found"));
        if (client != null) {
            final var response = new ClientResponse(
                    client.getName(),
                    client.getCpf(),
                    client.getPhone(),
                    client.getAddress(),
                    client.getJob(),
                    client.getRegisteredBy().getName()
            );
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    public Client registerClient(ClientRequest request, Long employee_id) {
        var employee = employeeRepository.findById(employee_id).orElseThrow(()-> new EntityNotFound("Employee not Found"));

        Client client = new ClientBuilder()
                .name(request.name())
                .cpf(request.cpf())
                .phone(request.phone())
                .birth(request.birth())
                .address(request.address())
                .job(request.job())
                .registeredBy(employee)
                .build();
        return clientRepository.save(client);
    }

    public Client updateClientData(Long clientId, Client request) {
        Client client1 = clientRepository.findById(clientId).get();
        BeanUtils.copyProperties(client1, request, "id");
        return clientRepository.save(client1);
    }
    public ResponseEntity<Client> removeClient(Long clientId) {
        try {
            exclude(clientId);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFound e) {
            return ResponseEntity.notFound().build();
        } catch (EntityInUse e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }
    public void exclude(Long clientId) {
        try {
            clientRepository.deleteById(clientId);
        } catch (EmptyResultDataAccessException e){
            throw new EntityNotFound("Client code % not found" + clientId);
        } catch (DataIntegrityViolationException e) {
            throw new EntityInUse("Client code % could be not removed," + clientId);
        }
    }
}
