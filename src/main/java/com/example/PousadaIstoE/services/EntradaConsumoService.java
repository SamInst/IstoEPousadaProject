package com.example.PousadaIstoE.services;

import com.example.PousadaIstoE.exceptions.EntityConflict;
import com.example.PousadaIstoE.exceptions.EntityNotFound;
import com.example.PousadaIstoE.model.EntradaConsumo;
import com.example.PousadaIstoE.model.Entradas;
import com.example.PousadaIstoE.repository.EntradaConsumoRepository;
import com.example.PousadaIstoE.repository.EntradaRepository;
import com.example.PousadaIstoE.response.EntradaConsumoResponse;
import com.example.PousadaIstoE.response.StatusEntrada;
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

    public List<EntradaConsumo> BuscaTodos() {
        return entradaConsumoRepository.findAll();
    }

    public List<EntradaConsumoResponse> consumoResponse(Long id, List<EntradaConsumoResponse> entradaConsumo) {
        final var consumo = entradaConsumoRepository.findById(id).orElseThrow(
                () -> new NoSuchElementException("Consumo não Encontrado"));
        entradaConsumo.forEach(a -> {
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

    public EntradaConsumo addConsumo(EntradaConsumo entradaConsumo) {
        if (entradaConsumo.getEntradas() == null) {
            throw new EntityNotFound("Nenhuma entrada associada a esse consumo");
        }
        if (entradaConsumo.getEntradas().getStatusEntrada().equals(StatusEntrada.FINALIZADA)){
            throw new EntityConflict("Não é possivel inserir consumo em uma entrada já finalizada");
        }
        final var item = itensFeing.findItensById(entradaConsumo.getItens().getId());
        Entradas entrada = entradaRepository.findById(entradaConsumo.getEntradas().getId())
                .orElseThrow(()-> new EntityNotFound("entrada não encontrada"));
        EntradaConsumo entradaConsumo1 = new EntradaConsumo(
                entradaConsumo.getQuantidade(),
                item,
                entrada
        );
        return entradaConsumoRepository.save(entradaConsumo1);
    }

    public void deletaConsumoPorEntradaId(Long id_consumo) {
        entradaConsumoRepository.deleteById(id_consumo);
    }

    public List<EntradaConsumo> findEntradaConsumoByEntrada(Long entrada_id){
        return entradaConsumoRepository.findEntradaConsumoByEntradas_Id(entrada_id);
    }
}
