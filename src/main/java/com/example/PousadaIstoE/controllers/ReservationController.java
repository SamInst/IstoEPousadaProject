package com.example.PousadaIstoE.controllers;

import com.example.PousadaIstoE.request.ReservationRequest;
import com.example.PousadaIstoE.request.UpdateReservationRequest;
import com.example.PousadaIstoE.response.ReservationResponse;
import com.example.PousadaIstoE.services.OvernightStayReservationService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/reservations")
public class ReservationController {
    private final OvernightStayReservationService reservationService;

    public ReservationController(OvernightStayReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public void createReservation(@RequestBody ReservationRequest request){
        reservationService.createReservation(request);
    }

    @PutMapping("/update/{reservation_id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void alterReservation(@PathVariable Long reservation_id, @RequestBody UpdateReservationRequest request){
        reservationService.alterReservation(reservation_id, request);
    }

    @GetMapping("/find/{reservation_id}")
    @ResponseStatus(HttpStatus.OK)
    public ReservationResponse findById(@PathVariable Long reservation_id){
        return reservationService.findReservationById(reservation_id);
    }

    @DeleteMapping("/remove_client/{reservation_id}/{client_id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeClientFromReservation(@PathVariable Long reservation_id, @PathVariable Long client_id) {
        reservationService.removeClientFromReservation(reservation_id, client_id);
    }

}
