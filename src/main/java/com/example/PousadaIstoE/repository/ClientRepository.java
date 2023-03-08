package com.example.PousadaIstoE.repository;

import com.example.PousadaIstoE.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientRepository extends JpaRepository <Client, Long> {
}