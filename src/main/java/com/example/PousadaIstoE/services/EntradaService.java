package com.example.PousadaIstoE.services;

import com.example.PousadaIstoE.exceptions.EntityConflict;
import com.example.PousadaIstoE.exceptions.EntityNotFound;
import com.example.PousadaIstoE.model.*;
import com.example.PousadaIstoE.repository.*;
import com.example.PousadaIstoE.response.EntradaResponse;
import com.example.PousadaIstoE.response.StatusPagamento;
import com.example.PousadaIstoE.response.TipoPagamento;
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
    double total;
    double valorEntrada;
    Duration diferenca;
    int horas;
    int minutosRestantes;
    String relatorio;
    double valorTotal;
    Entradas entradas;
    double entradaEConsumo;
    RegistroConsumoEntrada registroConsumoEntrada = new RegistroConsumoEntrada();
    List<EntradaConsumo> entradaConsumoList = new ArrayList<>();
    RegistroDeEntradas registroDeEntradas = new RegistroDeEntradas();
    List<RegistroConsumoEntrada> registroConsumoEntradaList = new ArrayList<>();
    private final EntradaRepository entradaRepository;
    private final PernoitesRepository pernoitesRepository;
    private final MapaGeralRepository mapaGeralRepository;
    private final RegistroDeEntradasRepository registroDeEntradasRepository;
    private final EntradaConsumoRepository entradaConsumoRepository;
    private final RegistroEntradaConsumoRepository registroEntradaConsumoRepository;

    public EntradaService(EntradaRepository entradaRepository, PernoitesRepository pernoitesRepository, MapaGeralRepository mapaGeralRepository, RegistroDeEntradasRepository registroDeEntradasRepository, EntradaConsumoRepository entradaConsumoRepository, RegistroEntradaConsumoRepository registroEntradaConsumoRepository) {
        this.entradaRepository = entradaRepository;
        this.pernoitesRepository = pernoitesRepository;
        this.mapaGeralRepository = mapaGeralRepository;
        this.registroDeEntradasRepository = registroDeEntradasRepository;
        this.entradaConsumoRepository = entradaConsumoRepository;
        this.registroEntradaConsumoRepository = registroEntradaConsumoRepository;
    }

    public List<Entradas> findAll() {
        return entradaRepository.findAll();
    }

    public ResponseEntity<AtomicReference<EntradaResponse>> findById(Long id) {
         AtomicReference<EntradaResponse> response = new AtomicReference<>();

         final var teste = entradaConsumoRepository.findEntradaConsumoByEntradas_Id(id);
         List<List<EntradaConsumo>> entradaConsumoList = new ArrayList<>();
         entradaConsumoList.add(teste);

        final var entrada = entradaRepository.findById(id).orElseThrow(() -> new EntityNotFound("Entrada não foi Cadastrada"));
        calcularHora();
             Double totalConsumo = manager.createQuery(
          "SELECT sum(m.total) FROM EntradaConsumo m where m.entradas.id = m.entradas.id", Double.class)
             .getSingleResult();
             if (totalConsumo == null){
                 totalConsumo = (double) 0;
             }
                double total0 = total + totalConsumo;

        response.set(new EntradaResponse(
                entrada.getApt(),
                entrada.getHoraEntrada(),
                entrada.getHoraSaida(),
                entrada.getPlaca(),
                new EntradaResponse.TempoPermanecido(
                        horas,
                        minutosRestantes
                ),
                entradaConsumoList,
                        totalConsumo,
                        valorEntrada,
                total0
        ));
        return ResponseEntity.ok(response);
    }

    public List<RegistroDeEntradas> findByData(LocalDate localDate){
        return registroDeEntradasRepository.findByData(localDate);
    }

    public Entradas registerEntrada(Entradas entradas) {
        entradas.setHoraEntrada(LocalTime.now());
        entradas.setHoraSaida(LocalTime.of(0,0));
        validacaoDeApartamento(entradas);
        return entradaRepository.save(entradas);
    }

    public void updateEntradaData(Long entradaId, Entradas request) {
        entradas = entradaRepository.findById(entradaId).orElseThrow(() -> new EntityNotFound("Entrada não encontrada"));
        entradaConsumoList = entradaConsumoRepository.findEntradaConsumoByEntradas_Id(entradaId);

        var entradaAtualizada = new Entradas();
             entradaAtualizada = new Entradas(
                    entradas.getId(),
                    entradas.getApt(),
                    entradas.getHoraEntrada(),
                    LocalTime.now(),
                    entradas.getPlaca(),
                    request.getTipoPagamento(),
                    request.getStatus_pagamento()
            );
        entradaRepository.save(entradaAtualizada);

        if (request.getStatus_pagamento().equals(StatusPagamento.CONCLUIDO)){
            if (request.getEntradaConsumo() == null){
                    consumoVazio(entradaAtualizada);
                }
            }
            calcularHora();
            validacaoPagamento(request);
            validacaoHorario();
            salvaNoMapa(request);
            registrarConsumoEntrada(registroConsumoEntradaList);
            registrarEntrada(request);

            entradaRepository.save(entradaAtualizada);
//            entradaRepository.deleteById(entradaId);
        }


    private void validacaoDeApartamento(Entradas entradas) throws EntityConflict {

       List<Pernoites> pernoites = pernoitesRepository.findAll();
         pernoites.forEach(apartamento -> {
             List<Entradas> listaDeApartamentos = entradaRepository.findByApt(entradas.getApt());
             List<Pernoites> listaDeApartamentosPernoite = pernoitesRepository.findByApt(apartamento.getApt());
             for (Entradas entradaCadastrada : listaDeApartamentos) {
                 for (Pernoites pernoiteCadastrado : listaDeApartamentosPernoite) {
                     if ( entradas.getApt().equals(entradaCadastrada.getApt())
                       || entradas.getHoraSaida() == null ) {
//                             && apartamento.getApt().equals(entradas.getApt())
//                             && apartamento.getApt().equals(pernoiteCadastrado.getApt())
                         throw new EntityConflict("O apartamento está ocupado no momento.");
                     }
//                     if (pernoiteCadastrado.getApt().equals(entradaCadastrada.getApt()))
////                             && apartamento.getApt().equals(pernoiteCadastrado.getApt()))
//                     {
//                         throw new EntityConflict("O apartamento está ocupado no momento.");
//                     }
                 }
             }
         });
    }

    private void calcularHora(){
        List<Entradas> entradas = entradaRepository.findAll();
        entradas.forEach(entradas1 -> {
            diferenca = Duration.between(entradas1.getHoraEntrada(), entradas1.getHoraSaida());

            long minutos = diferenca.toMinutes();
            horas = (int) (minutos / 60);
            minutosRestantes = (int) (minutos % 60);

            if (horas < 2 || (horas == 2 && minutosRestantes <= 20)) {
                total = 30.0;
            } else {
                total = 30.0 + ((horas - 2) * 7.0);
                if (minutosRestantes > 0) {
                    total += 40.0;
                    }
                }
            }
        );
        valorEntrada = total;
    }

    private void salvaNoMapa(Entradas request){
        MapaGeral mapaGeral = new MapaGeral(
        );
        mapaGeral.setApartment(entradas.getApt());
        mapaGeral.setData(LocalDate.now());
        mapaGeral.setEntrada((float) valorEntrada);
        mapaGeral.setReport(relatorio);
        mapaGeral.setTotal((float) entradaEConsumo);
        mapaGeral.setSaida(0F);
        mapaGeral.setHora(LocalTime.now());

        if (request.getTipoPagamento().equals(TipoPagamento.PIX)){
            mapaGeral.setReport(relatorio + " (PIX)");
            mapaGeral.setEntrada(0F);
            mapaGeral.setTotal((float) total);
        }
        if (request.getTipoPagamento().equals(TipoPagamento.CARTAO)){
            mapaGeral.setReport(relatorio + " (CARTAO)");
            mapaGeral.setEntrada(0F);
            mapaGeral.setTotal((float) total);
        }
        mapaGeralRepository.save(mapaGeral);
    }

    private void registrarConsumoEntrada(List<RegistroConsumoEntrada> registroConsumoEntradaList){
        entradaConsumoList.forEach(a-> {
            registroConsumoEntrada.setRegistroDeEntradas(registroDeEntradas);
            registroConsumoEntrada.setItens(a.getItens());
            registroConsumoEntrada.setQuantidade(a.getQuantidade());
            registroConsumoEntrada.setTotal(a.getTotal());

            registroEntradaConsumoRepository.save(registroConsumoEntrada);
            registroConsumoEntradaList.add(registroConsumoEntrada);

            Long consumo_id = a.getId();
//            entradaConsumoRepository.deleteById(consumo_id);
        });
    }

    public void validacaoHorario(){
        LocalTime noite = LocalTime.of(18,0,0);
        LocalTime dia = LocalTime.of(6,0,0);

        if (LocalTime.now().isAfter(noite) && LocalTime.now().isBefore(dia)){
            relatorio = "ENTRADA NOITE";
        } else {
            relatorio = "ENTRADA DIA";
        }
    }

    private void registrarEntrada(Entradas request){
        registroDeEntradas.setApt(entradas.getApt());
        registroDeEntradas.setHoraEntrada(entradas.getHoraEntrada());
        registroDeEntradas.setHoraSaida(entradas.getHoraSaida());
        registroDeEntradas.setConsumo(" ");
        registroDeEntradas.setPlaca(entradas.getPlaca());
        registroDeEntradas.setData(LocalDate.now());
        registroDeEntradas.setTipoPagamento(request.getTipoPagamento());
        registroDeEntradas.setStatus_pagamento(request.getStatus_pagamento());
        registroDeEntradas.setHoras(horas);
        registroDeEntradas.setMinutos(minutosRestantes);
        registroDeEntradas.setTotal(entradaEConsumo);
        registroDeEntradas.setEntradaConsumo(registroConsumoEntradaList);

        registroDeEntradasRepository.save(registroDeEntradas);
    }

    private void validacaoPagamento(Entradas request){
        Float total = manager.createQuery("SELECT m.total FROM MapaGeral m ORDER BY m.id DESC", Float.class)
        .setMaxResults(1)
        .getSingleResult();

        Double totalConsumo = manager.createQuery(
        "SELECT sum(m.total) FROM EntradaConsumo m where m.entradas.id = m.entradas.id", Double.class)
        .getSingleResult();

        if (totalConsumo == null){
            totalConsumo = (double) 0;
            consumoVazio(request);
        }
        entradaEConsumo = valorEntrada + totalConsumo;
        valorTotal = total + entradaEConsumo;
    }


   private void consumoVazio(Entradas request){
       Itens semConsumo = manager.createQuery("SELECT m FROM Itens m where  m.id = 8", Itens.class)
               .getSingleResult();
       EntradaConsumo novoConsumo = new EntradaConsumo();
       novoConsumo.setItens(semConsumo);

       List<EntradaConsumo> entradaConsumoList2 = new ArrayList<>();
       entradaConsumoList2.add(novoConsumo);
       request.setEntradaConsumo(entradaConsumoList2);
   }
}