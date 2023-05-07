package com.example.PousadaIstoE.services;

import com.example.PousadaIstoE.model.EntradaConsumo;
import com.example.PousadaIstoE.repository.EntradaConsumoRepository;
import com.example.PousadaIstoE.repository.EntradaRepository;
import com.example.PousadaIstoE.response.EntradaConsumoResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class EntradaConsumoService {
 private final EntradaConsumoRepository entradaConsumoRepository;

 private final EntradaRepository entradaRepository;

    public EntradaConsumoService(EntradaConsumoRepository entradaConsumoRepository, EntradaRepository entradaRepository) {
        this.entradaConsumoRepository = entradaConsumoRepository;
        this.entradaRepository = entradaRepository;
    }

    public List<EntradaConsumo> BuscaTodos(){
        return entradaConsumoRepository.findAll();
    }

    public List<EntradaConsumoResponse> consumoResponse(Long id, List<EntradaConsumoResponse> entradaConsumo){
        final var consumo = entradaConsumoRepository.findById(id).orElseThrow(
                ()-> new NoSuchElementException("Consumo não Encontrado"));


        entradaConsumo.forEach(a-> {
            Float valorItem = consumo.getQuantidade() * consumo.getItens().getValor();


                    new EntradaConsumoResponse(
                    consumo.getQuantidade(),
                    new EntradaConsumoResponse.Item(
                            consumo.getItens().getDescricao(),
                            consumo.getItens().getValor(),
                            valorItem
                            ),
                    a.total()
                    );

        });
        return entradaConsumo;
    }

    public EntradaConsumo addConsumo(EntradaConsumo entradaConsumo){
        EntradaConsumo entradaConsumo1 = new EntradaConsumo(
                entradaConsumo.getQuantidade(),
                entradaConsumo.getItens(),
                entradaConsumo.getEntradas()
        );
        return entradaConsumoRepository.save(entradaConsumo1);
    }

    public ResponseEntity<Object> deletaConsumoPorEntradaId(Long id_consumo){
        entradaConsumoRepository.deleteById(id_consumo);
        return ResponseEntity.noContent().build();
    }
}
