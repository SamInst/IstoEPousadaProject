package com.example.PousadaIstoE.services;

import com.example.PousadaIstoE.exceptions.EntityNotFound;
import com.example.PousadaIstoE.model.Entradas;
import com.example.PousadaIstoE.repository.EntradaRepository;
import com.example.PousadaIstoE.response.EntradaResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.util.List;


@Service
public class EntradaService {

    private final EntradaRepository entradaRepository;

    public EntradaService(EntradaRepository entradaRepository) {
        this.entradaRepository = entradaRepository;
    }

    public List<Entradas> findAll(){
        return entradaRepository.findAll();
    }

    public ResponseEntity<EntradaResponse> findById(Long id){
        final var entrada = entradaRepository.findById(1L).orElseThrow(()-> new EntityNotFound("Entrada não foi Cadastrada"));
        Float total = null;
            final var response = new EntradaResponse(
                    entrada.getApt(),
                    entrada.getHoraEntrada(),
                    entrada.getHoraSaida(),
                    entrada.getConsumo(),
                    entrada.getPlaca(),
                    total
            );
            return ResponseEntity.ok(response);
        }

        public Entradas registerEntrada(Entradas entradas){
        return entradaRepository.save(entradas);
        }

    }

