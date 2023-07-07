package com.example.PousadaIstoE.services;

import com.example.PousadaIstoE.exceptions.EntityConflict;
import com.example.PousadaIstoE.exceptions.EntityNotFound;
import com.example.PousadaIstoE.model.*;
import com.example.PousadaIstoE.repository.*;
import com.example.PousadaIstoE.response.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class EntradaService {
    @PersistenceContext
    private EntityManager manager;
    double totalHorasEntrada;
    double valorEntrada;
    Duration diferenca;
    int horas;
    int minutosRestantes;
    String relatorio;
    double valorTotal;
    Entradas entradas;
    double entradaEConsumo;
    Float totalMapaGeral;

    List<EntradaConsumo> entradaConsumoList = new ArrayList<>();
    private final EntradaRepository entradaRepository;
    private final MapaGeralRepository mapaGeralRepository;
    private final EntradaConsumoRepository entradaConsumoRepository;

    private final QuartosRepository quartosRepository;

    public EntradaService(
            EntradaRepository entradaRepository,
            MapaGeralRepository mapaGeralRepository,
            EntradaConsumoRepository entradaConsumoRepository,
            QuartosRepository quartosRepository) {
        this.entradaRepository = entradaRepository;
        this.mapaGeralRepository = mapaGeralRepository;
        this.entradaConsumoRepository = entradaConsumoRepository;
        this.quartosRepository = quartosRepository;
    }

    public List<EntradaSimplesResponse> findAll (){
        final var findAll = entradaRepository.findAll();
        List<EntradaSimplesResponse> entradaSimplesResponseList = new ArrayList<>();
        findAll.forEach( entradas ->{
            EntradaSimplesResponse entradaSimplesResponse = new EntradaSimplesResponse(
                    entradas.getId(),
                    entradas.getQuartos().getNumero(),
                    entradas.getHoraEntrada(),
                    entradas.getHoraSaida(),
                    entradas.getPlaca(),
                    entradas.getStatusEntrada()
            );
            entradaSimplesResponseList.add(entradaSimplesResponse);
        });
        return entradaSimplesResponseList;
    }

    public ResponseEntity<AtomicReference<EntradaResponse>> findById(Long id) {
        AtomicReference<EntradaResponse> response = new AtomicReference<>();
        entradaConsumoList = entradaConsumoRepository.findEntradaConsumoByEntradas_Id(id);

        final var entrada = entradaRepository.findById(id).orElseThrow(() -> new EntityNotFound("Entrada não foi Cadastrada ou não existe mais"));
        calcularHora();

        Double totalConsumo = manager.createQuery(
          "SELECT sum(m.total) FROM EntradaConsumo m where m.entradas.id = :id", Double.class)
                .setParameter("id", id)
                .getSingleResult();
        if (totalConsumo == null){
            totalConsumo = (double) 0;
        }
        double soma = totalConsumo + valorEntrada;
        List<ConsumoResponse> consumoResponseList = new ArrayList<>();
        entradaConsumoList.forEach(consumo -> {
            ConsumoResponse consumoResponse = new ConsumoResponse(
                    consumo.getQuantidade(),
                    consumo.getItens().getDescricao(),
                    consumo.getItens().getValor(),
                    consumo.getTotal()
            );
            consumoResponseList.add(consumoResponse);
        });
        response.set(new EntradaResponse(
                entrada.getQuartos().getNumero(),
                entrada.getHoraEntrada(),
                entrada.getHoraSaida(),
                entrada.getPlaca(),
                new EntradaResponse.TempoPermanecido(
                horas,
                minutosRestantes
                ),
                consumoResponseList,
                entrada.getStatusEntrada(),
                totalConsumo,
                valorEntrada,
                soma
        ));
        return ResponseEntity.ok(response);
    }

    public Entradas registerEntrada(Entradas entradas) {
        Quartos quartoOut = entradas.getQuartos();
        if (quartoOut.getStatusDoQuarto().equals(StatusDoQuarto.OCUPADO)){
            throw new EntityConflict("Quarto Ocupado");
        }
        if (quartoOut.getStatusDoQuarto().equals(StatusDoQuarto.NECESSITA_LIMPEZA)){
            throw new EntityConflict("Quarto Precisa de limpeza!");
        }
        if (quartoOut.getStatusDoQuarto().equals(StatusDoQuarto.RESERVADO)){
            throw new EntityConflict("Quarto Reservado!");
        }
        entradas.setHoraEntrada(LocalTime.now());
        entradas.setHoraSaida(LocalTime.of(0,0));
        entradas.setStatus_pagamento(StatusPagamento.PENDENTE);
        entradas.setTipoPagamento(TipoPagamento.PENDENTE);
        entradas.setStatusEntrada(StatusEntrada.EM_ANDAMENTO);

        quartoOut.setStatusDoQuarto(StatusDoQuarto.OCUPADO);
        quartosRepository.save(quartoOut);
        return entradaRepository.save(entradas);
    }

    public void updateEntradaData(Long entradaId, Entradas request) {
        entradas = entradaRepository.findById(entradaId).orElseThrow(() -> new EntityNotFound("Entrada não encontrada"));

        var entradaAtualizada = new Entradas(
                    entradas.getId(),
                    entradas.getQuartos(),
                    entradas.getHoraEntrada(),
                    LocalTime.now(),
                    entradas.getPlaca(),
                    request.getTipoPagamento(),
                    request.getStatus_pagamento(),
                    entradas.getStatusEntrada()
        );
        entradaRepository.save(entradaAtualizada);

        if (request.getStatus_pagamento().equals(StatusPagamento.CONCLUIDO)) {
            if (request.getStatus_pagamento().equals(StatusPagamento.CONCLUIDO)
                    && entradaAtualizada.getStatusEntrada().equals(StatusEntrada.FINALIZADA)){
                throw new EntityConflict("A Entrada já foi salva no mapa");
            }
            entradaConsumoList = entradaConsumoRepository.findEntradaConsumoByEntradas_Id(entradaId);
            calcularHora();
            if (entradaAtualizada.getEntradaConsumo() == null) {
                consumoVazio();
            }
            validacaoPagamento(entradas);
            validacaoHorario();
            salvaNoMapa(request);

            Quartos quartoOut = entradas.getQuartos();
            quartoOut.setStatusDoQuarto(StatusDoQuarto.DISPONIVEL);
            quartosRepository.save(quartoOut);
            entradaAtualizada.setStatusEntrada(StatusEntrada.FINALIZADA);
            entradaRepository.save(entradaAtualizada);
        }
    }

    private void calcularHora(){
        List<Entradas> entradas = entradaRepository.findAll();
        entradas.forEach(entradas1 -> {
                    diferenca = Duration.between(entradas1.getHoraEntrada(), entradas1.getHoraSaida());

                    long minutos = diferenca.toMinutes();
                    horas = (int) (minutos / 60);
                    minutosRestantes = (int) (minutos % 60);

                    if (horas < 2 || (horas == 2 && minutosRestantes <= 20)) {
                        totalHorasEntrada = 30.0;
                    } else {
                        totalHorasEntrada = 30.0 + ((horas - 2) * 10.0);
                        if (minutosRestantes > 0) {
                            totalHorasEntrada += 40.0;
                        }
                    }
                }
        );
        valorEntrada = totalHorasEntrada;
    }

    private void validacaoPagamento(Entradas request){
        totalMapaGeral = mapaGeralRepository.findLastTotal();

        Double totalConsumo = manager.createQuery(
                        "SELECT sum(m.total) FROM EntradaConsumo m where m.entradas.id = :id", Double.class)
                .setParameter("id", request.getId())
                .getSingleResult();

        entradaEConsumo = valorEntrada + totalConsumo;
        valorTotal = totalMapaGeral + entradaEConsumo;
    }

    private void validacaoHorario(){
        LocalTime noite = LocalTime.of(18,0,0);
        LocalTime dia = LocalTime.of(6,0,0);

        if (LocalTime.now().isAfter(noite) || LocalTime.now().isBefore(dia)){
            relatorio = "ENTRADA NOITE";
        } else {
            relatorio = "ENTRADA DIA";
        }
    }

    private void salvaNoMapa(Entradas request){
        MapaGeral mapaGeral = new MapaGeral(
        );
        mapaGeral.setApartment(entradas.getQuartos().getNumero());
        mapaGeral.setData(LocalDate.now());
        mapaGeral.setEntrada((float) entradaEConsumo);
        mapaGeral.setReport(relatorio);
        mapaGeral.setTotal((float) valorTotal);
        mapaGeral.setSaida(0F);
        mapaGeral.setHora(LocalTime.now());

        if (request.getTipoPagamento().equals(TipoPagamento.PIX)){
            mapaGeral.setReport(relatorio + " (PIX)");
            mapaGeral.setSaida(mapaGeral.getEntrada());
        }
        if (request.getTipoPagamento().equals(TipoPagamento.CARTAO)){
            mapaGeral.setReport(relatorio + " (CARTAO)");
            mapaGeral.setSaida(mapaGeral.getEntrada());
        }
        if (request.getTipoPagamento().equals(TipoPagamento.DINHEIRO)){
            mapaGeral.setReport(relatorio + " (DINHEIRO)");
        }
        mapaGeralRepository.save(mapaGeral);
    }

   private void consumoVazio(){
       Itens semConsumo = manager.createQuery("SELECT m FROM Itens m where  m.id = 8", Itens.class)
               .getSingleResult();
       EntradaConsumo novoConsumo = new EntradaConsumo();
       novoConsumo.setItens(semConsumo);
       novoConsumo.setQuantidade(0);
       novoConsumo.setTotal(0F);
       novoConsumo.setEntradas(entradas);
       entradaConsumoRepository.save(novoConsumo);
   }

   public List<Entradas> findByStatusEntrada(StatusEntrada statusEntrada){
        return entradaRepository.findEntradasByStatusEntrada(statusEntrada);
   }
}