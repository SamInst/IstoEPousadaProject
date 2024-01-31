package com.example.PousadaIstoE.services;

import com.example.PousadaIstoE.builders.ClientBuilder;
import com.example.PousadaIstoE.exceptions.EntityInUse;
import com.example.PousadaIstoE.exceptions.EntityNotFound;
import com.example.PousadaIstoE.model.Client;
import com.example.PousadaIstoE.repository.ClientRepository;
import com.example.PousadaIstoE.repository.EmployeeRepository;
import com.example.PousadaIstoE.request.ClientRequest;
import com.example.PousadaIstoE.response.ClientResponse;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClientService {
    private final ClientRepository clientRepository;
    private final Finder find;
    private final EmployeeRepository employeeRepository;

    public ClientService(ClientRepository clientRepository, Finder find, EmployeeRepository employeeRepository) {
        this.clientRepository = clientRepository;
        this.find = find;
        this.employeeRepository = employeeRepository;
    }

    public Page<ClientResponse> findAll(Pageable pageable){
        var clients = clientRepository.findAll(pageable);

        List<ClientResponse> clientResponseList = new ArrayList<>();

        clients.forEach(client -> {
            ClientResponse clientResponse = new ClientResponse(
                    client.getName(),
                    client.getCpf(),
                    client.getPhone(),
                    client.getAddress(),
                    client.getJob(),
                    client.getRegisteredBy().getName()
            );
            clientResponseList.add(clientResponse);
        });
        clients.stream().sorted(Comparator.comparing(Client::getName)).collect(Collectors.toList());

        return new PageImpl<>(clientResponseList, pageable, clients.getTotalElements());
    }
    public ClientResponse findClientById(Long client_id){
        final var client = find.clientById(client_id);

            return new ClientResponse(
                    client.getName() != null ? client.getName() : "",
                    client.getCpf() != null ? client.getCpf() : "",
                    client.getPhone() != null ? client.getPhone() : "",
                    client.getAddress() != null ? client.getAddress() : "",
                    client.getJob() != null ? client.getJob() : "",
                    client.getRegisteredBy().getName() != null ? client.getRegisteredBy().getName() : ""
            );
    }

    public Client registerClient(ClientRequest request, Long employee_id) {
        var employee = find.employeeById(employee_id);

        Client client = new ClientBuilder()
                .name(request.name().toUpperCase())
                .cpf(request.cpf())
                .phone(request.phone())
                .birth(request.birth())
                .address(request.address().toUpperCase())
                .job(request.job().toUpperCase())
                .registeredBy(employee)
                .build();
        return clientRepository.save(client);
    }

    public void updateClientData(ClientRequest request, Long client_id) {
        var client = find.clientById(client_id);

        Client updatedClient = new ClientBuilder()
                .id(client.getId())
                .name(request.name())
                .cpf(request.cpf())
                .phone(request.phone())
                .birth(request.birth())
                .address(request.address())
                .job(request.job())
                .registeredBy(client.getRegisteredBy())
                .build();
                clientRepository.save(updatedClient);
    }

    public void removeClient(Long client_id) {
        clientRepository.deleteById(client_id);
    }
}
