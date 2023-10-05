package com.example.PousadaIstoE.services;

import com.example.PousadaIstoE.exceptions.EntityNotFound;
import com.example.PousadaIstoE.model.Quartos;
import com.example.PousadaIstoE.repository.QuartosRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuartosService {
    Quartos quartos;

    private final QuartosRepository quartosRepository;

    public QuartosService(QuartosRepository quartosRepository) {
        this.quartosRepository = quartosRepository;
    }

    public List<Quartos> quartosList() {
        return quartosRepository.findAllOrOrderByNumero();
    }

    public Quartos findQuarto(Long id) {
        return quartosRepository.findById(id).orElseThrow(() -> new EntityNotFound("Quarto não existe"));
    }

    public Quartos createQuartos(Quartos quartos) {
        return quartosRepository.save(quartos);
    }

    public Quartos updateQuartoData(Long quartoId, Quartos request) {
        quartos = quartosRepository.findById(quartoId).orElseThrow(() -> new EntityNotFound("Quarto não encontrado"));

        var quartoAtualizado = new Quartos(
                quartos.getId(),
                quartos.getNumero(),
                quartos.getDescricao(),
                quartos.getCapacidadePessoa(),
                request.getStatusDoQuarto()
        );
        quartosRepository.save(quartoAtualizado);
        return quartoAtualizado;
    }
}
