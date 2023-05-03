package com.example.PousadaIstoE.services;

import com.example.PousadaIstoE.model.EntradaConsumo;
import com.example.PousadaIstoE.model.Entradas;
import com.example.PousadaIstoE.repository.EntradaConsumoRepository;
import com.example.PousadaIstoE.response.ConsumoResponse;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class EntradaConsumoService {
 private final EntradaConsumoRepository entradaConsumoRepository;

    public EntradaConsumoService(EntradaConsumoRepository entradaConsumoRepository) {
        this.entradaConsumoRepository = entradaConsumoRepository;
    }

    public List<EntradaConsumo> BuscaTodos(){
        return entradaConsumoRepository.findAll();
    }

    public List<ConsumoResponse> consumoResponse(Long id, List<ConsumoResponse> entradaConsumo){
        final var consumo = entradaConsumoRepository.findById(id).orElseThrow(
                ()-> new NoSuchElementException("Consumo não Encontrado"));


        entradaConsumo.forEach(a-> {
            Float valorItem = consumo.getQuantidade() * consumo.getItens().getValor();


                    new ConsumoResponse(
                    consumo.getQuantidade(),
                    new ConsumoResponse.Item(
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
}
