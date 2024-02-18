package com.example.PousadaIstoE.repository;

import com.example.PousadaIstoE.model.OvernightStay;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface OvernightStayRepository extends JpaRepository <OvernightStay, Long> {

    @Query("select u from OvernightStay u order by u.id desc ")
    Page<OvernightStay> findAllOrderByIdDesc(Pageable pageable);
    List<OvernightStay> findAllByRoom_Id(Long room_id);


}