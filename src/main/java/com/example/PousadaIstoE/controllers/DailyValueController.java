package com.example.PousadaIstoE.controllers;

import com.example.PousadaIstoE.model.DailyValues;
import com.example.PousadaIstoE.request.DailyRequest;
import com.example.PousadaIstoE.services.DailyValueService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/daily/values")
public class DailyValueController {
    private final DailyValueService dailyValueService;

    public DailyValueController(DailyValueService dailyValueService) {
        this.dailyValueService = dailyValueService;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<DailyValues> getAll(){
        return dailyValueService.getAll();
    }

    @PostMapping("/insert")
    @ResponseStatus(HttpStatus.CREATED)
    public void insertNewValue(@RequestBody DailyRequest request){
        dailyValueService.insertNewValue(request);
    }

    @PutMapping("/update/{daily_id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void alterValues(
            @PathVariable Long daily_id,
            @RequestBody  DailyRequest request){
        dailyValueService.alterValues(daily_id, request);
    }

    @DeleteMapping("/remove/{daily_id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeValue(@PathVariable Long daily_id){
        dailyValueService.removeValue(daily_id);
    }
}
