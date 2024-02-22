package com.example.PousadaIstoE.repository;

import com.example.PousadaIstoE.model.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerRepository extends JpaRepository <Customer, Long> {

    @Query(value = """
            select ip01_name
            from ip01_clients 
            where ip01_name ilike %:name%
            """, nativeQuery = true)
    List<Customer> autoCompleteNameClient(@Param("name") String name);


    Customer findClientByCpf(String cpf);

    @Query(value = """
            select ip01_cpf from ip01_clients
            """, nativeQuery = true)
    List<Customer> findallCpfs(String cpf);


    @Query(value = """
            select * from ip01_clients ip01 order by ip01.ip01_name
            """, nativeQuery = true)
    Page<Customer> findAllOrderByName(Pageable pageable);
}