package com.example.PousadaIstoE.controllers;

import com.example.PousadaIstoE.model.Entradas;
import com.example.PousadaIstoE.model.RegistroDeEntradas;
import com.example.PousadaIstoE.repository.RegistroDeEntradasRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/registro_entradas")
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
}
