package com.example.PousadaIstoE.controllers;

import com.example.PousadaIstoE.model.RegistroDeEntradas;
import com.example.PousadaIstoE.repository.RegistroDeEntradasRepository;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/registroEntradas")
public class RegistroDeEntradasController {
    private final RegistroDeEntradasRepository registroDeEntradasRepository;

    public RegistroDeEntradasController(RegistroDeEntradasRepository registroDeEntradasRepository) {
        this.registroDeEntradasRepository = registroDeEntradasRepository;
    }

    @GetMapping
    public List<RegistroDeEntradas> findAll(){
        return registroDeEntradasRepository.findAll();
    }

    @GetMapping("/{id}")
    public Optional<RegistroDeEntradas> findById(@PathVariable ("id") Long id){
        return registroDeEntradasRepository.findById(id);
    }
    @GetMapping("/buscar-por-data")
    public List<RegistroDeEntradas> findByData(LocalDate data_entrada){
        return registroDeEntradasRepository.findByData(data_entrada);
    }
}
