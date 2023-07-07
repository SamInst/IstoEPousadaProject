package com.example.PousadaIstoE.services;

import com.example.PousadaIstoE.Fixture.EntradaConsumoFixture;
import com.example.PousadaIstoE.Fixture.EntradasFixture;
import com.example.PousadaIstoE.Fixture.QuartosFixture;
import com.example.PousadaIstoE.exceptions.EntityConflict;
import com.example.PousadaIstoE.model.EntradaConsumo;
import com.example.PousadaIstoE.model.Entradas;
import com.example.PousadaIstoE.model.Quartos;
import com.example.PousadaIstoE.repository.EntradaConsumoRepository;
import com.example.PousadaIstoE.repository.EntradaRepository;
import com.example.PousadaIstoE.repository.MapaGeralRepository;
import com.example.PousadaIstoE.repository.QuartosRepository;
import com.example.PousadaIstoE.response.StatusDoQuarto;
import com.example.PousadaIstoE.response.StatusEntrada;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
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
    @Mock
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
    @DisplayName("deve registrar uma entrada")
    void registerEntrada() {
        when(entradaRepository.save(any())).thenReturn(entrada);
        entradaService.registerEntrada(entrada);
    }

    @Test
    @DisplayName("deve lancar uma exception de quarto ocupado")
    void registerEntradaExceptionOcupado() {
        entrada.getQuartos().setStatusDoQuarto(StatusDoQuarto.OCUPADO);
        when(entradaRepository.save(any())).thenReturn(entrada);

        assertThatExceptionOfType(EntityConflict.class)
        .isThrownBy(()->entradaService.registerEntrada(entrada)).
                withMessage("Quarto Ocupado");
    }

    @Test
    @DisplayName("deve lancar uma exception de quarto Sujo")
    void registerEntradaExceptionLimpesa() {
        entrada.getQuartos().setStatusDoQuarto(StatusDoQuarto.NECESSITA_LIMPEZA);
        when(entradaRepository.save(any())).thenReturn(entrada);

        assertThatExceptionOfType(EntityConflict.class)
                .isThrownBy(()->entradaService.registerEntrada(entrada)).
                withMessage("Quarto Precisa de limpeza!");
    }
    @Test
    @DisplayName("deve lancar uma exception de quarto reservado")
    void registerEntradaExceptionReservado() {
        entrada.getQuartos().setStatusDoQuarto(StatusDoQuarto.RESERVADO);
        when(entradaRepository.save(any())).thenReturn(entrada);

        assertThatExceptionOfType(EntityConflict.class)
                .isThrownBy(()->entradaService.registerEntrada(entrada)).
                withMessage("Quarto Reservado!");
    }

    @Test
    void updateEntradaData() {
    }

    @Test
    void findByStatusEntrada() {
        when(entradaRepository.findEntradasByStatusEntrada(StatusEntrada.EM_ANDAMENTO)).thenReturn(entradasList);
        entradaService.findByStatusEntrada(StatusEntrada.EM_ANDAMENTO);
        verify(entradaRepository, atLeastOnce()).findEntradasByStatusEntrada(StatusEntrada.EM_ANDAMENTO);
    }
}