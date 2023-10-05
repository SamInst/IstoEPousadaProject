package com.example.PousadaIstoE.model;

import com.example.PousadaIstoE.response.StatusPagamento;
import com.example.PousadaIstoE.response.StatusPernoite;
import com.example.PousadaIstoE.response.TipoPagamento;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "tb_pernoites")
public class Pernoites {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private Quartos apartamento;
    @OneToOne
    private Client clientePrincipal;
    private LocalDate dataEntrada;
    private LocalDate dataSaida;
    private Integer quantidadePessoa;
    private TipoPagamento tipoPagamento;
    private StatusPagamento status_pagamento;
    private StatusPernoite statusPernoite;
    private Float total;

    @OneToMany
    private List<Client> clienteAcompanhante;

    public List<Client> getClienteAcompanhante() {
        return clienteAcompanhante;
    }

    public void setClienteAcompanhante(List<Client> clienteAcompanhante) {
        this.clienteAcompanhante = clienteAcompanhante;
    }

    public StatusPernoite getStatusPernoite() { return statusPernoite; }
    public void setStatusPernoite(StatusPernoite statusPernoite) { this.statusPernoite = statusPernoite; }
    public Float getTotal() { return total; }
    public void setTotal(Float total) {this.total = total; }
    public Long getId() {return id;}
    public void setId(Long id) { this.id = id; }
    public Integer getQuantidadePessoa() { return quantidadePessoa; }
    public void setQuantidadePessoa(Integer quantidadePessoa) {this.quantidadePessoa = quantidadePessoa;}
    public LocalDate getDataEntrada() {return dataEntrada; }
    public LocalDate getDataSaida() {return dataSaida; }
    public Client getClientePrincipal() {return clientePrincipal; }
    public void setDataEntrada(LocalDate dataEntrada) { this.dataEntrada = dataEntrada; }
    public void setDataSaida(LocalDate dataSaida) { this.dataSaida = dataSaida; }
    public void setClientePrincipal(Client clientePrincipal) { this.clientePrincipal = clientePrincipal; }
    public TipoPagamento getTipoPagamento() { return tipoPagamento; }
    public StatusPagamento getStatus_pagamento() { return status_pagamento; }
    public Quartos getApartamento() { return apartamento; }
    public void setApartamento(Quartos apartamento) { this.apartamento = apartamento; }
    public void setTipoPagamento(TipoPagamento tipoPagamento) { this.tipoPagamento = tipoPagamento; }
    public void setStatus_pagamento(StatusPagamento status_pagamento) { this.status_pagamento = status_pagamento; }

    public Pernoites(Long id, Quartos apartamento, Client clientePrincipal, LocalDate dataEntrada, LocalDate dataSaida, Integer quantidadePessoa, TipoPagamento tipoPagamento, StatusPagamento status_pagamento, Float total) {
        this.id = id;
        this.apartamento = apartamento;
        this.clientePrincipal = clientePrincipal;
        this.dataEntrada = dataEntrada;
        this.dataSaida = dataSaida;
        this.quantidadePessoa = quantidadePessoa;
        this.tipoPagamento = tipoPagamento;
        this.status_pagamento = status_pagamento;
        this.total = total;
    }



    public Pernoites() {
    }
}