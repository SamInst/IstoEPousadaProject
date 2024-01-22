package com.example.PousadaIstoE.services;

import com.example.PousadaIstoE.exceptions.EntityNotFound;
import com.example.PousadaIstoE.model.Rooms;
import com.example.PousadaIstoE.repository.RoomRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoomService {
    Rooms rooms;

    private final RoomRepository roomRepository;

    public RoomService(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    public List<Rooms> quartosList() {
        return roomRepository.findAll();
    }

    public Rooms findQuarto(Long id) {
        return roomRepository.findById(id).orElseThrow(() -> new EntityNotFound("Quarto não existe"));
    }

    public Rooms createQuartos(Rooms rooms) {
        return roomRepository.save(rooms);
    }

    public Rooms updateQuartoData(Long quartoId, Rooms request) {
        rooms = roomRepository.findById(quartoId).orElseThrow(() -> new EntityNotFound("Quarto não encontrado"));

        var quartoAtualizado = new Rooms(
                rooms.getId(),
                rooms.getNumber(),
                rooms.getDescription(),
                rooms.getPersonCapacity(),
                request.getRoomStatus()
        );
        roomRepository.save(quartoAtualizado);
        return quartoAtualizado;
    }
}
