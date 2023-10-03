package com.example.PousadaIstoE.controllers;

import com.example.PousadaIstoE.exceptions.EntityNotFound;
import com.example.PousadaIstoE.model.Client;
import com.example.PousadaIstoE.repository.ClientRepository;
import com.example.PousadaIstoE.response.ClienteResponse;
import com.example.PousadaIstoE.services.ClientServices;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/clients")
public class ClientController {
    private  final ClientServices clientServices;

    public ClientController(ClientServices clientServices) {
        this.clientServices = clientServices;
    }

    @GetMapping
    public List<Client>list(){
        return clientServices.findAll();
    }
    @GetMapping("/{id}")
    public ResponseEntity<ClienteResponse> findClient(@PathVariable("id") Long id){
        return clientServices.findClientById(id);
    }
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/create")
    public Client register(Client client) {
        return clientServices.registerClient(client);
    }

    @PutMapping("/{clientId}")
    public Client AlterClientData(@PathVariable Long clientId, @RequestBody Client client) {
       return clientServices.updateClientData(clientId, client);
    }
    @DeleteMapping("/{clientId}")
    public ResponseEntity<Client> removeClient(@PathVariable Long clientId) {
       return clientServices.removeClient(clientId);
    }
}
