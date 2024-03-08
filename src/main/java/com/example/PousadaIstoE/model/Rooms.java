package com.example.PousadaIstoE.model;

import com.example.PousadaIstoE.Enums.RoomStatus;
import com.example.PousadaIstoE.Enums.RoomType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "ip04_rooms")
public class Rooms {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ip04_id")
    private Long id;
    @Column(name = "ip04_number")
    private Integer number;

    @Column(name = "ip04_description")
    private String description;

    @Column(name = "ip04_person_capacity")
    private Integer personCapacity;

    @Column(name = "ip04_room_status")
    private RoomStatus roomStatus;

    @Column(name = "ip04_room_type")
    private RoomType roomType;
}
