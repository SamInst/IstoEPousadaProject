package com.example.PousadaIstoE.repository;

import com.example.PousadaIstoE.model.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {
    @Query("select m from Item m where m.id = 8")
    Item getEmptyItem();
}