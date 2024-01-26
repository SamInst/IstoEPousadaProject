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
    List<Entry> findEntriesByEntryStatus(EntryStatus entryStatus);

    List<Entry> findEntriesByEntryDataRegister(LocalDate localDate);

    @Query("select sum(m.total) from EntryConsumption m where m.entry.id = :entry_id")
    Double totalConsumption(Long entry_id);
}