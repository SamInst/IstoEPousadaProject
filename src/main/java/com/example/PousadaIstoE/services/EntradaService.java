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
    Float totalMapaGeral;
    double totalHorasEntrada;
    double valorEntrada;
    Duration diferenca;
    int horas;
    int minutosRestantes;
    String relatorio;
    double valorTotal;
    Entradas entradas;
    double entradaEConsumo;

    List<EntradaConsumo> entradaConsumoList = new ArrayList<>();
    private final EntradaRepository entradaRepository;
    private final PernoitesRepository pernoitesRepository;
    private final MapaGeralRepository mapaGeralRepository;
    private final RegistroDeEntradasRepository registroDeEntradasRepository;
    private final EntradaConsumoRepository entradaConsumoRepository;
    private final RegistroEntradaConsumoRepository registroEntradaConsumoRepository;
    private final QuartosRepository quartosRepository;

    public EntradaService(EntradaRepository entradaRepository, PernoitesRepository pernoitesRepository, MapaGeralRepository mapaGeralRepository, RegistroDeEntradasRepository registroDeEntradasRepository, EntradaConsumoRepository entradaConsumoRepository, RegistroEntradaConsumoRepository registroEntradaConsumoRepository, QuartosRepository quartosRepository) {
        this.entradaRepository = entradaRepository;
        this.pernoitesRepository = pernoitesRepository;
        this.mapaGeralRepository = mapaGeralRepository;
        this.registroDeEntradasRepository = registroDeEntradasRepository;
        this.entradaConsumoRepository = entradaConsumoRepository;
        this.registroEntradaConsumoRepository = registroEntradaConsumoRepository;
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
                    entradas.getPlaca()
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
          "SELECT sum(m.total) FROM EntradaConsumo m where m.entradas.id = m.entradas.id", Double.class)
             .getSingleResult();
        if (totalConsumo == null){
            totalConsumo = (double) 0;
        }
                double total0 = totalHorasEntrada + totalConsumo;

        response.set(new EntradaResponse(
                entrada.getQuartos().getNumero(),
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

    public Entradas registerEntrada(Entradas entradas) {
        Quartos quartoOut = entradas.getQuartos();
        if (quartoOut.getStatusDoQuarto().equals(StatusDoQuarto.OCUPADO)){
            throw new EntityConflict("Quarto Ocupado");
        }
        entradas.setHoraEntrada(LocalTime.now());
        entradas.setHoraSaida(LocalTime.of(0,0));
        entradas.setStatus_pagamento(StatusPagamento.PENDENTE);
        entradas.setTipoPagamento(TipoPagamento.PENDENTE);
//        validacaoDeApartamento(entradas);


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
                    request.getStatus_pagamento()
            );
        entradaRepository.save(entradaAtualizada);

        if (request.getStatus_pagamento().equals(StatusPagamento.CONCLUIDO)) {
            entradaConsumoList = entradaConsumoRepository.findEntradaConsumoByEntradas_Id(entradaId);
            calcularHora();
            validacaoPagamento(request);
            validacaoHorario();
            salvaNoMapa(request);


//            List<RegistroConsumoEntrada> registroConsumoEntradaList = new ArrayList<>();
//            RegistroConsumoEntrada registroConsumoEntrada = new RegistroConsumoEntrada();
//            entradaConsumoList.forEach(consumo -> {
//              registroConsumoEntrada.setEntradaConsumoList(entradaConsumoList);
//                registroEntradaConsumoRepository.save(registroConsumoEntrada);
//            });
//            registroConsumoEntradaList.add(registroConsumoEntrada);

//            RegistroDeEntradas registroDeEntradas = new RegistroDeEntradas();
//            registroDeEntradas.setApt(entradas.getApt());
//            registroDeEntradas.setHoraEntrada(entradas.getHoraEntrada());
//            registroDeEntradas.setHoraSaida(entradas.getHoraSaida());
//            registroDeEntradas.setPlaca(entradas.getPlaca());
//            registroDeEntradas.setData(LocalDate.now());
//            registroDeEntradas.setTipoPagamento(request.getTipoPagamento());
//            registroDeEntradas.setStatus_pagamento(request.getStatus_pagamento());
//            registroDeEntradas.setHoras(horas);
//            registroDeEntradas.setMinutos(minutosRestantes);
//            registroDeEntradas.setTotal(entradaEConsumo);
//            registroDeEntradas.setEntradaConsumo(registroConsumoEntradaList);

//            registroDeEntradasRepository.save(registroDeEntradas);

            Quartos quartoOut = entradas.getQuartos();
            quartoOut.setStatusDoQuarto(StatusDoQuarto.DISPONIVEL);
            quartosRepository.save(quartoOut);
            entradaRepository.save(entradaAtualizada);
//            excluirEntradaEConsumo(entradaId);
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
                        totalHorasEntrada = 30.0 + ((horas - 2) * 7.0);
                        if (minutosRestantes > 0) {
                            totalHorasEntrada += 40.0;
                        }
                    }
                }
        );
        valorEntrada = totalHorasEntrada;
    }

    private void validacaoPagamento(Entradas request){
        totalMapaGeral = manager.createQuery("SELECT m.total FROM MapaGeral m ORDER BY m.id DESC", Float.class)
                .setMaxResults(1)
                .getSingleResult();

        Double totalConsumo = manager.createQuery(
                        "SELECT sum(m.total) FROM EntradaConsumo m where m.entradas.id = m.entradas.id", Double.class)
                .getSingleResult();

        if (request.getEntradaConsumo() == null) {
            totalConsumo = (double) 0;
            consumoVazio();
        }
        entradaEConsumo = valorEntrada + totalConsumo;
        valorTotal = totalMapaGeral + entradaEConsumo;
    }

    public void validacaoHorario(){
        LocalTime noite = LocalTime.of(18,0,0);
        LocalTime dia = LocalTime.of(6,0,0);

        if (LocalTime.now().isAfter(noite) || LocalTime.now().isBefore(dia)){
            relatorio = "ENTRADA NOITE";
        } else {
            relatorio = "ENTRADA DIA";
        }
    }

    private void salvaNoMapa(Entradas request){

        var novoTotalMapaGeral = totalMapaGeral + entradaEConsumo;
        MapaGeral mapaGeral = new MapaGeral(
        );
        mapaGeral.setApartment(request.getQuartos().getNumero());
        mapaGeral.setData(LocalDate.now());
        mapaGeral.setEntrada((float) entradaEConsumo);
        mapaGeral.setReport(relatorio);
        mapaGeral.setTotal((float) novoTotalMapaGeral);
        mapaGeral.setSaida(0F);
        mapaGeral.setHora(LocalTime.now());

        if (request.getTipoPagamento().equals(TipoPagamento.PIX)){
            mapaGeral.setReport(relatorio + " (PIX)");
            mapaGeral.setEntrada(0F);
            mapaGeral.setTotal(totalMapaGeral);
        }
        if (request.getTipoPagamento().equals(TipoPagamento.CARTAO)){
            mapaGeral.setReport(relatorio + " (CARTAO)");
            mapaGeral.setEntrada(0F);
            mapaGeral.setTotal((float) totalHorasEntrada);
        }
        if (request.getTipoPagamento().equals(TipoPagamento.DINHEIRO)){
            mapaGeral.setReport(relatorio + " (DINHEIRO)");
        }
        mapaGeralRepository.save(mapaGeral);
    }



//    private void validacaoDeApartamento(Entradas entradas) throws EntityConflict {
//
//       List<Pernoites> pernoites = pernoitesRepository.findAll();
//         pernoites.forEach(apartamento -> {
//             List<Entradas> listaDeApartamentos = entradaRepository.findByQuartos_Numero(entradas.getQuartos().getNumero());
//             List<Pernoites> listaDeApartamentosPernoite = pernoitesRepository.findByApartamento_Id(apartamento.getApartamento().getId());
//             for (Entradas entradaCadastrada : listaDeApartamentos) {
//                 for (Pernoites pernoiteCadastrado : listaDeApartamentosPernoite) {
//                     if ( entradas.getQuartos().getNumero().equals(entradaCadastrada.getQuartos().getNumero())
//                       || entradas.getHoraSaida() == null ) {
////                             && apartamento.getApt().equals(entradas.getApt())
////                             && apartamento.getApt().equals(pernoiteCadastrado.getApt())
//                         throw new EntityConflict("O apartamento está ocupado no momento.");
//                     }
//                     apartamento.getApartamento().setStatusDoQuarto(StatusDoQuarto.OCUPADO);
////                     if (pernoiteCadastrado.getApt().equals(entradaCadastrada.getApt()))
//////                             && apartamento.getApt().equals(pernoiteCadastrado.getApt()))
////                     {
////                         throw new EntityConflict("O apartamento está ocupado no momento.");
////                     }
//                 }
//             }
//         });
//    }

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

   private void excluirEntradaEConsumo(Long entradaId){
       entradaConsumoRepository.deleteEntradaConsumoByEntradas_Id(entradaId);
       entradaRepository.deleteById(entradaId);
   }
}