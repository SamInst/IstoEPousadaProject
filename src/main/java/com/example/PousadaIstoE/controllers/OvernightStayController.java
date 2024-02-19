package com.example.PousadaIstoE.controllers;

import com.example.PousadaIstoE.request.OvernightStayRequest;
import com.example.PousadaIstoE.response.OvernightStayResponse;
import com.example.PousadaIstoE.response.SimpleOvernightResponse;
import com.example.PousadaIstoE.services.OvernightService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/overnights")
public class OvernightStayController {
    private final OvernightService overnightService;

    public OvernightStayController(OvernightService overnightService) { this.overnightService = overnightService; }

    @GetMapping("/find_by_id/{overnight_id}")
    public OvernightStayResponse findById(@PathVariable Long overnight_id){
        return overnightService.findById(overnight_id);
    }
    public Page<SimpleOvernightResponse> findAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size){
        Pageable pageable = PageRequest.of(page, size);
        return overnightService.findAll(pageable);
    }

    @PutMapping("/reservation_to_overnight/{reservation_id}")
    public void changeReservationToOvernight(@PathVariable Long reservation_id){
        overnightService.changeReservationToOvernight(reservation_id);
    }

    @PostMapping("/create")
    public void createOvernightStay(@RequestBody OvernightStayRequest request){
        overnightService.createOvernightStay(request);
    }

    @PutMapping("/update/{overnight_id}")
    private void updateOvernightStay(@PathVariable Long overnight_id, @RequestBody OvernightStayRequest request){
        overnightService.updateOvernightStay(overnight_id, request);
    }

}
