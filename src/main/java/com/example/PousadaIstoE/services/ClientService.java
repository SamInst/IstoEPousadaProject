package com.example.PousadaIstoE.services;

import com.example.PousadaIstoE.builders.ClientBuilder;
import com.example.PousadaIstoE.custom.QueryClient;
import com.example.PousadaIstoE.exceptions.EntityConflict;
import com.example.PousadaIstoE.model.Client;
import com.example.PousadaIstoE.model.Country;
import com.example.PousadaIstoE.model.County;
import com.example.PousadaIstoE.model.States;
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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ClientService {
    private static final String NE = "Not Specified";
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
            rs.getString("cpf"),
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
        Country country = null;
        States state = null;
        County county = null;
        if (request.country_id() != null) country = find.countryById(request.country_id());
        if (request.state_id() != null) state = find.stateById(request.state_id());
        if (request.county_id() != null) county = find.countyById(request.county_id());

        Client client = new ClientBuilder()
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
                .isHosted(client.isHosted())
                .registeredBy(client.getRegisteredBy())
                .build();
        clientRepository.save(updatedClient);
    }

    public void inactivateClient(Long client_id) {
        var client = find.clientById(client_id);
        client.setHosted(false);
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
                client.getRegisteredBy() != null ? client.getRegisteredBy().getName() : "",
                client.getCountry() != null ? client.getCountry().getDescription() : "",
                client.getState() != null ? client.getState().getDescription() : "",
                client.getCounty() != null ? client.getCounty().getDescription() : "",
                client.isHosted()
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

    public List<Client> clientRequest(ClientRequest request){
        List<Client> clientList = new ArrayList<>();
        var replacedCpf = find.replace(request.cpf());
        Client findClient = clientRepository.findClientByCpf(replacedCpf);

        if (findClient == null) {
            findClient = clientBuilder(request);
            clientList.add(findClient);
            clientRepository.save(findClient);
        } else {
            clientList.add(findClient);
        }
        return clientList;
    }

    public Client clientBuilder(ClientRequest client){
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
    public Set<Client> clientListVerification(List<Client> clientList){
        Set<Client> uniqueClients = new HashSet<>(clientList);
        if (uniqueClients.size() < clientList.size()) {
            throw new EntityConflict("The customer has already been added to this list.");
        }
        return uniqueClients;
    }

    public String replaceCPF(String cpf){
        return cpf.replaceAll("[.-]", "");
    }
}
