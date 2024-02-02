package com.example.PousadaIstoE.services;

import com.example.PousadaIstoE.builders.ClientBuilder;
import com.example.PousadaIstoE.custom.QueryClient;
import com.example.PousadaIstoE.model.Client;
import com.example.PousadaIstoE.repository.ClientRepository;
import com.example.PousadaIstoE.request.ClientRequest;
import com.example.PousadaIstoE.response.AutoCompleteNameResponse;
import com.example.PousadaIstoE.response.ClientResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClientService {
    private final ClientRepository clientRepository;
    private final JdbcTemplate jdbcTemplate;
    private final QueryClient queryClient;
    private final Finder find;

    public ClientService(
            ClientRepository clientRepository,
            JdbcTemplate jdbcTemplate,
            QueryClient queryClient,
            Finder find) {
        this.clientRepository = clientRepository;
        this.jdbcTemplate = jdbcTemplate;
        this.queryClient = queryClient;
        this.find = find;
    }
    static RowMapper<AutoCompleteNameResponse> rowMapperAutoCompleteName = (rs, rowNum) -> new AutoCompleteNameResponse(
            rs.getString("name"));

    public Page<ClientResponse> findAll(Pageable pageable) {
        var clients = clientRepository.findAllOrderByName(pageable);

        List<ClientResponse> clientResponseList = clients.stream()
                .map(this::clientResponse)
                .collect(Collectors.toList());
        return new PageImpl<>(clientResponseList, pageable, clients.getTotalElements());
    }

    public ClientResponse findClientById(Long client_id) {
        final var client = find.clientById(client_id);
        return clientResponse(client);
    }

    public Client registerClient(ClientRequest request, Long employee_id) {
        var employee = find.employeeById(employee_id);

        Client client = new ClientBuilder()
                .name(request.name().toUpperCase())
                .cpf(replaceCPF(request.cpf()))
                .phone(request.phone())
                .birth(request.birth())
                .address(request.address().toUpperCase())
                .job(request.job().toUpperCase())
                .active(true)
                .registeredBy(employee)
                .build();
        return clientRepository.save(client);
    }

    public void updateClientData(ClientRequest request, Long client_id) {
        var client = find.clientById(client_id);

        Client updatedClient = new ClientBuilder()
                .id(client.getId())
                .name(request.name())
                .cpf(replaceCPF(request.cpf()))
                .phone(request.phone())
                .birth(request.birth())
                .address(request.address())
                .job(request.job())
                .active(client.isActive())
                .registeredBy(client.getRegisteredBy())
                .build();
        clientRepository.save(updatedClient);
    }

    public void inactivateClient(Long client_id) {
        var client = find.clientById(client_id);
        client.setActive(false);
        clientRepository.save(client);
    }

    private ClientResponse clientResponse(Client client){
        return new ClientResponse(
                client.getId(),
                client.getName() != null ? client.getName() : "",
                client.getCpf() != null ? client.getCpf() : "",
                client.getPhone() != null ? client.getPhone() : "",
                client.getAddress() != null ? client.getAddress() : "",
                client.getJob() != null ? client.getJob() : "",
                client.getRegisteredBy().getName() != null ? client.getRegisteredBy().getName() : "",
                client.isActive()
        );
    }

    public ClientResponse findByCPF (String cpf){
        var client = clientRepository.findClientByCpf(replaceCPF(cpf));
        return clientResponse(client);
    }

    public List<AutoCompleteNameResponse> autoCompleteNameResponse(String name){
        var sql = queryClient.autoCompleteName(name.toUpperCase());
        return jdbcTemplate.query(sql, rowMapperAutoCompleteName);
    }

    public String replaceCPF(String cpf){
        return cpf.replaceAll("[.-]", "");
    }
}
