package com.example.PousadaIstoE.services;

import com.example.PousadaIstoE.builders.ClientBuilder;
import com.example.PousadaIstoE.custom.QueryCustomer;
import com.example.PousadaIstoE.exceptions.EntityConflict;
import com.example.PousadaIstoE.model.Customer;
import com.example.PousadaIstoE.model.Country;
import com.example.PousadaIstoE.model.County;
import com.example.PousadaIstoE.model.States;
import com.example.PousadaIstoE.repository.CustomerRepository;
import com.example.PousadaIstoE.request.ConsumerRequest;
import com.example.PousadaIstoE.response.AutoCompleteNameResponse;
import com.example.PousadaIstoE.response.ConsumerResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CostumerService {
    private static final String NE = "Not Specified";
    private final CustomerRepository customerRepository;
    private final JdbcTemplate jdbcTemplate;
    private final QueryCustomer queryCustomer;
    private final Finder find;

    public CostumerService(
            CustomerRepository customerRepository,
            JdbcTemplate jdbcTemplate,
            QueryCustomer queryCustomer,
            Finder find) {
        this.customerRepository = customerRepository;
        this.jdbcTemplate = jdbcTemplate;
        this.queryCustomer = queryCustomer;
        this.find = find;
    }
    static RowMapper<AutoCompleteNameResponse> rowMapperAutoCompleteName = (rs, rowNum) -> new AutoCompleteNameResponse(
            rs.getString("cpf"),
            rs.getString("name"));

    public Page<ConsumerResponse> findAll(Pageable pageable) {
        var clients = customerRepository.findAllOrderByName(pageable);

        List<ConsumerResponse> consumerResponseList = clients.stream()
                .map(this::customerResponse)
                .collect(Collectors.toList());
        return new PageImpl<>(consumerResponseList, pageable, clients.getTotalElements());
    }

    public ConsumerResponse findCustomerById(Long client_id) {
        final var client = find.clientById(client_id);
        return customerResponse(client);
    }

    public Customer registerCustomer(ConsumerRequest request, Long employee_id) {
        var employee = find.employeeById(employee_id);
        Country country = null;
        States state = null;
        County county = null;
        if (request.country_id() != null) country = find.countryById(request.country_id());
        if (request.state_id() != null) state = find.stateById(request.state_id());
        if (request.county_id() != null) county = find.countyById(request.county_id());

        Customer customer = new ClientBuilder()
                .name(request.name().toUpperCase())
                .cpf(replaceCPF(request.cpf()))
                .email(request.email())
                .phone(request.phone())
                .birth(request.birth())
                .address(request.address().toUpperCase())
                .job(request.job().toUpperCase())
                .isHosted(false)
                .isBlocked(false)
                .obs(request.obs())
                .registeredBy(employee)
                .country(country)
                .state(state)
                .county(county)
                .build();
        return customerRepository.save(customer);
    }

    public void updateCustomerData(ConsumerRequest request, Long client_id) {
        var client = find.clientById(client_id);

        Customer updatedCustomer = new ClientBuilder()
                .id(client.getId())
                .name(request.name())
                .cpf(replaceCPF(request.cpf()))
                .phone(request.phone())
                .birth(request.birth())
                .address(request.address())
                .job(request.job())
                .isHosted(client.isHosted())
                .registeredBy(client.getRegisteredBy())
                .build();
        customerRepository.save(updatedCustomer);
    }

    public void customerHosted(Customer customer, boolean hosted){
        customer.setHosted(hosted);
        customerRepository.save(customer);
    }

    public ConsumerResponse customerResponse(Customer customer){
        return new ConsumerResponse(
                customer.getId(),
                customer.getName() != null ? customer.getName() : NE,
                customer.getCpf() != null ? customer.getCpf() : NE,
                customer.getPhone() != null ? customer.getPhone() : NE,
                customer.getAddress() != null ? customer.getAddress() : NE,
                customer.getJob() != null ? customer.getJob() : NE,
                customer.getRegisteredBy() != null ? customer.getRegisteredBy().getName() : NE,
                customer.getCountry() != null ? customer.getCountry().getDescription() : NE,
                customer.getState() != null ? customer.getState().getDescription() : NE,
                customer.getCounty() != null ? customer.getCounty().getDescription() : NE,
                customer.isHosted()
        );
    }

    public ConsumerResponse findByCPF (String cpf){
        var client = customerRepository.findClientByCpf(replaceCPF(cpf));
        return customerResponse(client);
    }

    public List<AutoCompleteNameResponse> autoCompleteNameResponse(String name){
        var sql = queryCustomer.autoCompleteName(name.toUpperCase());
        return jdbcTemplate.query(sql, rowMapperAutoCompleteName);
    }

    public void customerVerification(ConsumerRequest request, List<Customer> customerList){
        var replacedCpf = find.replace(request.cpf());
        Customer findCustomer = customerRepository.findClientByCpf(replacedCpf);

        if (findCustomer == null) {
            findCustomer = customerBuilder(request);
            customerList.add(findCustomer);
            customerRepository.save(findCustomer);
        } else {
            customerList.add(findCustomer);
        }
    }

    public Customer customerBuilder(ConsumerRequest client){
        return new ClientBuilder()
                .name(client.name() != null ? find.replace(client.name()) : NE)
                .cpf(client.cpf() != null ? find.replace(client.cpf()) : NE)
                .phone(client.phone() != null ? find.replace(client.phone()) : NE)
                .birth(client.birth())
                .address(client.address() != null ? find.replace(client.address()) : NE)
                .job(client.job() != null ? find.replace(client.job()) : NE)
                .isHosted(false)
                .build();
    }
    public void customerListVerification(List<Customer> customerList){
        Set<Customer> uniqueCustomers = new HashSet<>(customerList);
        if (uniqueCustomers.size() < customerList.size()) {
            throw new EntityConflict("The customer has already been added to this list.");
        }
    }

    public String replaceCPF(String cpf){
        return cpf.replaceAll("[.-]", "");
    }
}
