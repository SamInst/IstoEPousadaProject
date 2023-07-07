package com.example.PousadaIstoE.services;

import com.example.PousadaIstoE.Fixture.ItensFixture;
import com.example.PousadaIstoE.model.Itens;
import com.example.PousadaIstoE.repository.ItensRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import java.util.List;
import java.util.Optional;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class ItensServiceTest {
    @Mock
    ItensRepository itensRepository;

    @InjectMocks
    ItensService itensService;

    List<Itens> itensList = ItensFixture.itensList();
    Itens itens = ItensFixture.itens();
    Itens itenVazio = ItensFixture.itenVazio();
    Long item_id = 1L;
    public static final Long ITEM_VAZIO = 8L;
    @Test
    void findAll() {
        when(itensRepository.findAll()).thenReturn(itensList);
        itensService.findAll();
        verify(itensRepository, atLeastOnce()).findAll();
    }

    @Test
    void findItensById() {
        when(itensRepository.findById(item_id)).thenReturn(Optional.ofNullable(itens));
        itensService.findItensById(item_id);
        verify(itensRepository, atLeastOnce()).findById(item_id);
    }

    @Test
    void criaItens() {
        when(itensRepository.save(any())).thenReturn(itens);
        itensService.criaItens(itens);
        verify(itensRepository, times(1)).save(itens);
    }

    @Test
    void itemConsumoVazio() {
        when(itensRepository.findById(ITEM_VAZIO)).thenReturn(Optional.ofNullable(itenVazio));
        itensService.itemConsumoVazio();
        verify(itensRepository, atLeastOnce()).findById(ITEM_VAZIO);
    }
}