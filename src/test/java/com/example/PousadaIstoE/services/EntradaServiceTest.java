package com.example.PousadaIstoE.services;

import com.example.PousadaIstoE.Fixture.EntradaConsumoFixture;
import com.example.PousadaIstoE.Fixture.EntradasFixture;
import com.example.PousadaIstoE.Fixture.QuartosFixture;
import com.example.PousadaIstoE.model.EntradaConsumo;
import com.example.PousadaIstoE.model.Entradas;
import com.example.PousadaIstoE.model.Quartos;
import com.example.PousadaIstoE.repository.EntradaConsumoRepository;
import com.example.PousadaIstoE.repository.EntradaRepository;
import com.example.PousadaIstoE.repository.MapaGeralRepository;
import com.example.PousadaIstoE.repository.QuartosRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import java.util.List;
import java.util.Optional;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class EntradaServiceTest {
    @Mock
    EntradaRepository entradaRepository;
    @Mock
    MapaGeralRepository mapaGeralRepository;
    @Mock
    EntradaConsumoRepository entradaConsumoRepository;
    @Mock
    QuartosRepository quartosRepository;
    @Mock
    EntityManager manager;

    @InjectMocks
    EntradaService entradaService;
    List<Entradas> entradasList = EntradasFixture.entradasList();
    Entradas entrada = EntradasFixture.entradas();
    Long entrada_id = 1L;
    Quartos quartoDisponivel = QuartosFixture.quartoDisponivel();
    Quartos quartoOceupado = QuartosFixture.quartoOcupado();
    private TypedQuery query;
    List<EntradaConsumo> entradaConsumosList= EntradaConsumoFixture.entradaConsumoList();

    @Test
    void findAll() {
        when(entradaRepository.findAll()).thenReturn(entradasList);
        entradaService.findAll();
        verify(entradaRepository, atLeastOnce()).findAll();
    }

    @Test
    void findById() {
        manager = mock(EntityManager.class);
        query = mock(TypedQuery.class);

        when(entradaRepository.findById(entrada_id)).thenReturn(Optional.ofNullable(entrada));
        when(entradaConsumoRepository.findEntradaConsumoByEntradas_Id(entrada_id)).thenReturn(entradaConsumosList);
        entradaService.findById(entrada_id);
    }

    @Test
    void registerEntrada() {
        when(entradaRepository.save(any())).thenReturn(entrada);
        when(quartosRepository.save(any())).thenReturn(quartoOceupado);
        entradaService.registerEntrada(entrada);
    }

    @Test
    void updateEntradaData() {
    }

    @Test
    void validacaoHorario() {
    }

    @Test
    void findByStatusEntrada() {
    }
}