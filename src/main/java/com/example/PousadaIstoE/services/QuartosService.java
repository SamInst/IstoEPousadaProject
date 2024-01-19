package com.example.PousadaIstoE.services;

import com.example.PousadaIstoE.exceptions.EntityNotFound;
import com.example.PousadaIstoE.model.Rooms;
import com.example.PousadaIstoE.repository.QuartosRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuartosService {
    Rooms rooms;

    private final QuartosRepository quartosRepository;

    public QuartosService(QuartosRepository quartosRepository) {
        this.quartosRepository = quartosRepository;
    }

    public List<Rooms> quartosList() {
        return quartosRepository.findAll();
    }

    public Rooms findQuarto(Long id) {
        return quartosRepository.findById(id).orElseThrow(() -> new EntityNotFound("Quarto não existe"));
    }

    public Rooms createQuartos(Rooms rooms) {
        return quartosRepository.save(rooms);
    }

    public Rooms updateQuartoData(Long quartoId, Rooms request) {
        rooms = quartosRepository.findById(quartoId).orElseThrow(() -> new EntityNotFound("Quarto não encontrado"));

        var quartoAtualizado = new Rooms(
                rooms.getId(),
                rooms.getNumero(),
                rooms.getDescricao(),
                rooms.getCapacidadePessoa(),
                request.getStatusDoQuarto()
        );
        quartosRepository.save(quartoAtualizado);
        return quartoAtualizado;
    }
}
