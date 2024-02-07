package com.example.PousadaIstoE.repository;

import com.example.PousadaIstoE.model.Country;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CountryRepository extends JpaRepository<Country, Long> {
}
