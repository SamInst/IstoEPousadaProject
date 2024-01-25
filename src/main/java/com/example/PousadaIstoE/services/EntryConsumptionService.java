package com.example.PousadaIstoE.services;

import com.example.PousadaIstoE.exceptions.EntityConflict;
import com.example.PousadaIstoE.exceptions.EntityNotFound;
import com.example.PousadaIstoE.model.EntryConsumption;
import com.example.PousadaIstoE.model.Entry;
import com.example.PousadaIstoE.repository.EntryConsumptionRepository;
import com.example.PousadaIstoE.repository.EntryRepository;
import com.example.PousadaIstoE.response.EntryConsumptionResponse;
import com.example.PousadaIstoE.Enums.EntryStatus;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class EntryConsumptionService {
    private final EntryConsumptionRepository entryConsumptionRepository;
    private final ItemService itensFeing;
    private final EntryRepository entryRepository;
    public EntryConsumptionService(
        EntryConsumptionRepository entryConsumptionRepository,
        ItemService itensFeing,
        EntryRepository entryRepository) {
        this.entryConsumptionRepository = entryConsumptionRepository;
        this.itensFeing = itensFeing;
        this.entryRepository = entryRepository;
    }

    public List<EntryConsumption> BuscaTodos() {
        return entryConsumptionRepository.findAll();
    }

    public List<EntryConsumptionResponse> consumoResponse(Long id, List<EntryConsumptionResponse> entradaConsumo) {
        final var consumo = entryConsumptionRepository.findById(id).orElseThrow(
                () -> new NoSuchElementException("Consumo não Encontrado"));
        entradaConsumo.forEach(a -> {
            Float valorItem = consumo.getAmount() * consumo.getItens().getValue();

            new EntryConsumptionResponse(
                    consumo.getAmount(),
                    new EntryConsumptionResponse.Item(
                            consumo.getItens().getDescription(),
                            consumo.getItens().getValue(),
                            valorItem
                    ),
                    a.total()
            );
        });
        return entradaConsumo;
    }

    public EntryConsumption addConsumo(EntryConsumption entryConsumption) {
        if (entryConsumption.getEntradas() == null) {
            throw new EntityNotFound("Nenhuma entrada associada a esse consumption");
        }
        if (entryConsumption.getEntradas().getEntryStatus().equals(EntryStatus.FINISH)){
            throw new EntityConflict("Não é possivel inserir consumption em uma entrada já finalizada");
        }
        final var item = itensFeing.findItensById(entryConsumption.getItens().getId());
        Entry entrada = entryRepository.findById(entryConsumption.getEntradas().getId())
                .orElseThrow(()-> new EntityNotFound("entrada não encontrada"));
        EntryConsumption entryConsumption1 = new EntryConsumption(
                entryConsumption.getAmount(),
                item,
                entrada
        );
        return entryConsumptionRepository.save(entryConsumption1);
    }

    public void deletaConsumoPorEntradaId(Long id_consumo) {
        entryConsumptionRepository.deleteById(id_consumo);
    }

    public List<EntryConsumption> findEntradaConsumoByEntrada(Long entrada_id){
        return entryConsumptionRepository.findEntradaConsumoByEntradas_Id(entrada_id);
    }
}
