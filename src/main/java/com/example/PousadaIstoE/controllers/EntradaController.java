package com.example.PousadaIstoE.controllers;

import com.example.PousadaIstoE.model.Entradas;
import com.example.PousadaIstoE.response.EntradaResponse;
import com.example.PousadaIstoE.response.EntradaSimplesResponse;
import com.example.PousadaIstoE.response.StatusEntrada;
import com.example.PousadaIstoE.services.EntradaService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@RestController
@RequestMapping("/entradas")
public class EntradaController {
    private final EntradaService entradaService;
    public EntradaController(EntradaService entradaService) {
        this.entradaService = entradaService;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Page<EntradaSimplesResponse> findAll(
    @RequestParam(defaultValue = "0") int page,
    @RequestParam(defaultValue = "10") int size,
    @RequestParam(defaultValue = "id") String sortField
    ) {
        Sort.Order sortOrder = Sort.Order.desc(sortField);
        Sort sort = Sort.by(sortOrder);
        Pageable pageable = PageRequest.of(page, size, sort);
        return entradaService.findAll(pageable);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public AtomicReference<EntradaResponse> findById(@PathVariable ("id") Long id){
        return entradaService.findById(id);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/register")
    public Entradas entradas(Entradas entradas){
        return entradaService.registerEntrada(entradas);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    void atualizarEntrada (@PathVariable ("id") Long entradaID, @RequestBody Entradas entradas){
         entradaService.updateEntradaData(entradaID, entradas);
    }
    @GetMapping("/findByStatusEntrada")
    @ResponseStatus(HttpStatus.OK)
    public List<Entradas> findByStatus(StatusEntrada statusEntrada){
        return entradaService.findByStatusEntrada(statusEntrada);
    }
    @GetMapping("/findEntradaHoje")
    @ResponseStatus(HttpStatus.OK)
    public List<Entradas> findEntradaToday(){
        return entradaService.findEntradaByToday();
    }

    @GetMapping("/findEntradaByData")
    @ResponseStatus(HttpStatus.OK)
    public List<Entradas> findEntradaByData(LocalDate data_entrada) {
        return entradaService.findEntradaByDate(data_entrada);
    }
}
