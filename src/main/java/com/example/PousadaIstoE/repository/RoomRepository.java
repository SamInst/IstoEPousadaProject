package com.example.PousadaIstoE.repository;

import com.example.PousadaIstoE.Enums.RoomStatus;
import com.example.PousadaIstoE.model.Rooms;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomRepository extends JpaRepository<Rooms, Long> {

    List<Rooms> findAllByRoomStatus(RoomStatus roomStatus);
}
