package com.example.PousadaIstoE.controllers;

import com.example.PousadaIstoE.model.Client;
import com.example.PousadaIstoE.request.ClientRequest;
import com.example.PousadaIstoE.response.AutoCompleteNameResponse;
import com.example.PousadaIstoE.response.ClientResponse;
import com.example.PousadaIstoE.services.ClientService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/clients")
public class ClientController {
    private final ClientService clientService;

    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Page<ClientResponse> list(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return clientService.findAll(pageable);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/find/{client_id}")
    public ClientResponse findClientByID(@PathVariable Long client_id) {
        return clientService.findClientById(client_id);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/create/{employee_id}")
    public Client registerClient(@RequestBody ClientRequest request, @PathVariable Long employee_id) {
        return clientService.registerClient(request, employee_id);
    }

    @PutMapping("/update/{client_id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void updateClientData(@RequestBody ClientRequest request, @PathVariable Long client_id) {
        clientService.updateClientData(request, client_id);
    }

    @DeleteMapping("/inactivate/{client_id}")
    public void inactivateClient(@PathVariable Long client_id) {
        clientService.inactivateClient(client_id);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/find_by_name")
    public List<AutoCompleteNameResponse> autoCompleteNameResponse(String name){
        return clientService.autoCompleteNameResponse(name);
    }
}
