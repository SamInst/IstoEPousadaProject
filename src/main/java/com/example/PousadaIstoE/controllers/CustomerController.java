package com.example.PousadaIstoE.controllers;

import com.example.PousadaIstoE.model.Customer;
import com.example.PousadaIstoE.request.ConsumerRequest;
import com.example.PousadaIstoE.response.AutoCompleteNameResponse;
import com.example.PousadaIstoE.response.CustomerResponse;
import com.example.PousadaIstoE.services.CustomerService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/clients")
public class CustomerController {
    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Page<CustomerResponse> list(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return customerService.findAll(pageable);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/find/{client_id}")
    public CustomerResponse findClientByID(@PathVariable Long client_id) {
        return customerService.findCustomerById(client_id);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/create/{employee_id}")
    public Customer registerClient(@RequestBody ConsumerRequest request, @PathVariable Long employee_id) {
        return customerService.registerCustomer(request, employee_id);
    }

    @PutMapping("/update/{client_id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void updateClientData(@RequestBody ConsumerRequest request, @PathVariable Long client_id) {
        customerService.updateCustomerData(request, client_id);
    }

//    @DeleteMapping("/inactivate/{client_id}")
//    public void inactivateClient(@PathVariable Long client_id) {
//        clientService.inactivateClient(client_id);
//    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/find_by_name")
    public List<AutoCompleteNameResponse> autoCompleteNameResponse(String name){
        return customerService.autoCompleteNameResponse(name);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/find_by_cpf")
    public CustomerResponse findByCpf(String cpf){
        return customerService.findByCPF(cpf);
    }
}
