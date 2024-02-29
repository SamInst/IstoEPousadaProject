package com.example.PousadaIstoE.repository;

import com.example.PousadaIstoE.model.Entry;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface EntryRepository extends JpaRepository<Entry, Long> {

    List<Entry> findAllByEntryDataRegister(LocalDate date);

    @Query("select sum(m.total) from EntryConsumption m where m.entry.id = :entry_id")
    Double totalConsumptionByEntryId(Long entry_id);

    @Query("select u from Entry u order by u.id desc")
    Page<Entry> findAllOrderByIdDesc(Pageable pageable);

    @Query("select u from Entry u where u.active = true order by u.id desc")
    Page<Entry> findAllOrderByIdDescActive(Pageable pageable);
}