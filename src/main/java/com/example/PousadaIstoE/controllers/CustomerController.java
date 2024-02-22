package com.example.PousadaIstoE.controllers;

import com.example.PousadaIstoE.model.Customer;
import com.example.PousadaIstoE.request.ConsumerRequest;
import com.example.PousadaIstoE.response.AutoCompleteNameResponse;
import com.example.PousadaIstoE.response.ConsumerResponse;
import com.example.PousadaIstoE.services.CostumerService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/clients")
public class CustomerController {
    private final CostumerService costumerService;

    public CustomerController(CostumerService costumerService) {
        this.costumerService = costumerService;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Page<ConsumerResponse> list(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return costumerService.findAll(pageable);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/find/{client_id}")
    public ConsumerResponse findClientByID(@PathVariable Long client_id) {
        return costumerService.findCustomerById(client_id);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/create/{employee_id}")
    public Customer registerClient(@RequestBody ConsumerRequest request, @PathVariable Long employee_id) {
        return costumerService.registerCustomer(request, employee_id);
    }

    @PutMapping("/update/{client_id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void updateClientData(@RequestBody ConsumerRequest request, @PathVariable Long client_id) {
        costumerService.updateCustomerData(request, client_id);
    }

//    @DeleteMapping("/inactivate/{client_id}")
//    public void inactivateClient(@PathVariable Long client_id) {
//        clientService.inactivateClient(client_id);
//    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/find_by_name")
    public List<AutoCompleteNameResponse> autoCompleteNameResponse(String name){
        return costumerService.autoCompleteNameResponse(name);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/find_by_cpf")
    public ConsumerResponse findByCpf(String cpf){
        return costumerService.findByCPF(cpf);
    }
}
