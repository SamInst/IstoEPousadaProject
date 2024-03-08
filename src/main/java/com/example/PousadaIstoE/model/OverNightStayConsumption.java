package com.example.PousadaIstoE.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "ip10_overnight_consumption")
public class OverNightStayConsumption {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ip10_id")
    private Long id;

    @Column(name = "ip10_amount")
    private Integer amount;

    @ManyToOne
    @JoinColumn(name = "fkip10ip07_itens_id")
    private Item item;

    @ManyToOne
    @JoinColumn(name = "fkip10ip08_overnight_stay_id")
    private OvernightStay overnightStay;

    @Column(name = "ip10_total")
    private Float total;

    public OverNightStayConsumption(Integer amount, Item item, OvernightStay overnightStay) {
        this.amount = amount;
        this.item = item;
        this.overnightStay = overnightStay;
        this.total = amount.floatValue() * item.getValue();
    }
}