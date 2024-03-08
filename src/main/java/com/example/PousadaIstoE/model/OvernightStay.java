package com.example.PousadaIstoE.model;

import com.example.PousadaIstoE.Enums.OvernightStayStatus;
import com.example.PousadaIstoE.Enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "ip08_overnight_stay")
public class OvernightStay {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ip08_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "fkip08ip04_rooms_id")
    private Rooms room;

    @OneToMany
    @JoinColumn(name = "fkip08ip01_client_list")
    private List<Customer> customerList;

    @Column(name = "ip08_start_date")
    private LocalDate startDate;

    @Column(name = "ip08_end_date")
    private LocalDate endDate;

    @Column(name = "ip08_amount_people")
    private Integer amountPeople;

    @Column(name = "ip08_total_consumption")
    private Float totalConsumption;

    @Column(name = "ip08_overnight_value")
    private Float overnightValue;

    @Column(name = "ip08_total")
    private Float total;

    @Column(name = "ip08_active")
    private boolean isActive;

    @Column(name = "ip08_obs")
    private String obs;

    @Column(name = "ip08_overnight_status")
    private OvernightStayStatus overnightStayStatus;
}