package com.example.PousadaIstoE.services;

import com.example.PousadaIstoE.exceptions.EntityConflict;
import com.example.PousadaIstoE.exceptions.EntityNotFound;
import com.example.PousadaIstoE.model.EntradaConsumption;
import com.example.PousadaIstoE.model.Entradas;
import com.example.PousadaIstoE.repository.EntradaConsumoRepository;
import com.example.PousadaIstoE.repository.EntradaRepository;
import com.example.PousadaIstoE.response.EntradaConsumoResponse;
import com.example.PousadaIstoE.response.EntradaStatus;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class EntradaConsumoService {
    private final EntradaConsumoRepository entradaConsumoRepository;
    private final ItensService itensFeing;
    private final EntradaRepository entradaRepository;
    public EntradaConsumoService(
        EntradaConsumoRepository entradaConsumoRepository,
        ItensService itensFeing,
        EntradaRepository entradaRepository) {
        this.entradaConsumoRepository = entradaConsumoRepository;
        this.itensFeing = itensFeing;
        this.entradaRepository = entradaRepository;
    }

    public List<EntradaConsumption> BuscaTodos() {
        return entradaConsumoRepository.findAll();
    }

    public List<EntradaConsumoResponse> consumoResponse(Long id, List<EntradaConsumoResponse> entradaConsumo) {
        final var consumo = entradaConsumoRepository.findById(id).orElseThrow(
                () -> new NoSuchElementException("Consumo não Encontrado"));
        entradaConsumo.forEach(a -> {
            Float valorItem = consumo.getQuantity() * consumo.getItens().getValue();

            new EntradaConsumoResponse(
                    consumo.getQuantity(),
                    new EntradaConsumoResponse.Item(
                            consumo.getItens().getDescription(),
                            consumo.getItens().getValue(),
                            valorItem
                    ),
                    a.total()
            );
        });
        return entradaConsumo;
    }

    public EntradaConsumption addConsumo(EntradaConsumption entradaConsumption) {
        if (entradaConsumption.getEntradas() == null) {
            throw new EntityNotFound("Nenhuma entrada associada a esse consumo");
        }
        if (entradaConsumption.getEntradas().getStatusEntrada().equals(EntradaStatus.FINALIZADA)){
            throw new EntityConflict("Não é possivel inserir consumo em uma entrada já finalizada");
        }
        final var item = itensFeing.findItensById(entradaConsumption.getItens().getId());
        Entradas entrada = entradaRepository.findById(entradaConsumption.getEntradas().getId())
                .orElseThrow(()-> new EntityNotFound("entrada não encontrada"));
        EntradaConsumption entradaConsumption1 = new EntradaConsumption(
                entradaConsumption.getQuantity(),
                item,
                entrada
        );
        return entradaConsumoRepository.save(entradaConsumption1);
    }

    public void deletaConsumoPorEntradaId(Long id_consumo) {
        entradaConsumoRepository.deleteById(id_consumo);
    }

    public List<EntradaConsumption> findEntradaConsumoByEntrada(Long entrada_id){
        return entradaConsumoRepository.findEntradaConsumoByEntradas_Id(entrada_id);
    }
}
