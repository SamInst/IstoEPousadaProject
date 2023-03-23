package com.example.PousadaIstoE.services;

import com.example.PousadaIstoE.exceptions.EntityInUse;
import com.example.PousadaIstoE.exceptions.EntityNotFound;
import com.example.PousadaIstoE.model.Client;
import com.example.PousadaIstoE.repository.ClientRepository;
import com.example.PousadaIstoE.response.ClienteResponse;
import org.springframework.beans.BeanUtils;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Service
public class ClientServices {
    private final ClientRepository clientRepository;

    public ClientServices(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    public Client save(Client client) {
        return clientRepository.save(client);
    }

    public List<Client> findAll(){
        return clientRepository.findAll();
    }
    public ResponseEntity<ClienteResponse> findClientById(@PathVariable("id") Long id){
        final var client = clientRepository.findById(id).orElseThrow(() -> new EntityNotFound("Client not found"));
        if (client != null) {
            final var response = new ClienteResponse(
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

    public Client registerClient(@RequestBody Client client) {
        return clientRepository.save(client);
    }

    public Client updateClientData(@PathVariable Long clientId, @RequestBody Client client) {
        Client client1 = clientRepository.findById(clientId).get();
        BeanUtils.copyProperties(client1, client, "id");
        return clientRepository.save(client1);
    }
    public ResponseEntity<Client> removeClient(@PathVariable Long clientId) {
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
