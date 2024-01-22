package com.example.PousadaIstoE.repository;

import com.example.PousadaIstoE.model.Entry;
import com.example.PousadaIstoE.Enums.EntryStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface EntryRepository extends JpaRepository<Entry, Long> {
    List<Entry> findEntradasByStatusEntrada(EntryStatus entryStatus);
    List<Entry> findEntradasByDataRegistroEntrada(LocalDate localDate);
    @Query("select sum(m.total) from EntryConsumption m where m.entradas.id = :id_entrada")
    Double totalConsumo(Long id_entrada);
    @Query("select u from Entry u where u.dataRegistroEntrada = :data")
    List<Entry> findEntradasByData(LocalDate data);
}