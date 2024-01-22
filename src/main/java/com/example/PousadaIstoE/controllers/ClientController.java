package com.example.PousadaIstoE.controllers;

import com.example.PousadaIstoE.model.Client;
import com.example.PousadaIstoE.response.ClientResponse;
import com.example.PousadaIstoE.services.ClientService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/clients")
public class ClientController {
    private  final ClientService clientService;

    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @GetMapping
    public List<Client>list(){
        return clientService.findAll();
    }
    @GetMapping("/{id}")
    public ResponseEntity<ClientResponse> findClient(@PathVariable("id") Long id){
        return clientService.findClientById(id);
    }
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/create")
    public Client register(Client client) {
        return clientService.registerClient(client);
    }

    @PutMapping("/{clientId}")
    public Client AlterClientData(@PathVariable Long clientId, @RequestBody Client client) {
       return clientService.updateClientData(clientId, client);
    }
    @DeleteMapping("/{clientId}")
    public ResponseEntity<Client> removeClient(@PathVariable Long clientId) {
       return clientService.removeClient(clientId);
    }
}
