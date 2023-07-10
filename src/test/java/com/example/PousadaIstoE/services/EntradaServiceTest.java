package com.example.PousadaIstoE.services;

import com.example.PousadaIstoE.Fixture.EntradaConsumoFixture;
import com.example.PousadaIstoE.Fixture.EntradasFixture;
import com.example.PousadaIstoE.Fixture.ItensFixture;
import com.example.PousadaIstoE.exceptions.EntityConflict;
import com.example.PousadaIstoE.exceptions.EntityNotFound;
import com.example.PousadaIstoE.model.EntradaConsumo;
import com.example.PousadaIstoE.model.Entradas;
import com.example.PousadaIstoE.model.Itens;
import com.example.PousadaIstoE.repository.*;
import com.example.PousadaIstoE.response.StatusDoQuarto;
import com.example.PousadaIstoE.response.StatusEntrada;
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
    ItensRepository itensRepository;

    @InjectMocks
    EntradaService entradaService;
    List<Entradas> entradasList = EntradasFixture.entradasList();
    Entradas entrada = EntradasFixture.entradas();
    Entradas entradaEmAndamento = EntradasFixture.entradasEmAndamento();
    Entradas entradasEmAndamentoWithLongHours = EntradasFixture.entradasEmAndamentoWithLongHours();
    Entradas entradasEmAndamentoWithLongHoursDinheiro = EntradasFixture.entradasEmAndamentoWithLongHoursDInheiro();
    Long entrada_id = 1L;
    List<EntradaConsumo> entradaConsumosList= EntradaConsumoFixture.entradaConsumoList();
    Itens itemVazio = ItensFixture.itenVazio();

    @Test
    void findAll() {
        when(entradaRepository.findAll()).thenReturn(entradasList);
        entradaService.findAll();
        verify(entradaRepository, atLeastOnce()).findAll();
    }

    @Test
    void findById() {
        when(entradaRepository.findById(entrada_id)).thenReturn(Optional.ofNullable(entrada));
        when(entradaConsumoRepository.findEntradaConsumoByEntradas_Id(entrada_id)).thenReturn(entradaConsumosList);
        when(entradaRepository.totalConsumo(entrada_id)).thenReturn(anyDouble());
        entradaService.findById(entrada_id);
        verify(entradaRepository, atLeastOnce()).findById(entrada_id);
        verify(entradaConsumoRepository, atLeastOnce()).findEntradaConsumoByEntradas_Id(entrada_id);
        verify(entradaRepository, atLeastOnce()).totalConsumo(entrada_id);
    }
    @Test
    @DisplayName("should throw a exception when entrada has not found")
    void exceptionFindById() {
        when(entradaRepository.findById(entrada_id)).thenReturn(Optional.empty());

        assertThatExceptionOfType(EntityNotFound.class)
                .isThrownBy(()->entradaService.findById(entrada_id)).
                withMessage("Entrada não foi Cadastrada ou não existe mais");
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
        .isThrownBy(()->entradaService.registerEntrada(entrada))
        .withMessage("Quarto Ocupado");
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
    @DisplayName("should update entrada by id and save")
    void updateEntradaData() {
        when(entradaRepository.findById(entrada_id)).thenReturn(Optional.ofNullable(entrada));
        entradaService.updateEntradaData(entrada_id, entradaEmAndamento);
    }

    @Test
    void updateEntradaDataWithALongHours() {
        when(entradaRepository.findById(entrada_id)).thenReturn(Optional.ofNullable(entradasEmAndamentoWithLongHours));
        when(itensRepository.getItenVazio()).thenReturn(itemVazio);
        entradaService.updateEntradaData(entrada_id, entradasEmAndamentoWithLongHours);
    }

    @Test
    @DisplayName("should throw a exception when the entry has already been saved on map")
    void ExceptionUpdateEntradaData() {
        when(entradaRepository.findById(entrada_id)).thenReturn(Optional.ofNullable(entrada));
        assertThatExceptionOfType(EntityConflict.class)
                .isThrownBy(()->entradaService.updateEntradaData(entrada_id,entrada)).
                withMessage("A Entrada já foi salva no mapa");
    }

    @Test
    void findByStatusEntrada() {
        when(entradaRepository.findEntradasByStatusEntrada(StatusEntrada.EM_ANDAMENTO)).thenReturn(entradasList);
        entradaService.findByStatusEntrada(StatusEntrada.EM_ANDAMENTO);
        verify(entradaRepository, atLeastOnce()).findEntradasByStatusEntrada(StatusEntrada.EM_ANDAMENTO);
    }
}