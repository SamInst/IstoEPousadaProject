package com.example.PousadaIstoE.controllers;

import com.example.PousadaIstoE.request.EntryRequest;
import com.example.PousadaIstoE.request.UpdateEntryRequest;
import com.example.PousadaIstoE.response.EntryResponse;
import com.example.PousadaIstoE.response.SimpleEntryResponse;
import com.example.PousadaIstoE.services.EntryService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/entrys")
public class EntryController {
    private final EntryService entryService;

    public EntryController(EntryService entryService) {
        this.entryService = entryService;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Page<SimpleEntryResponse> findAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return entryService.findAll(pageable);
    }

    @GetMapping("/find/id/{entry_id}")
    @ResponseStatus(HttpStatus.OK)
    public EntryResponse findById(@PathVariable Long entry_id){
        return entryService.findById(entry_id);
    }

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public void createEntry(@RequestBody EntryRequest request){
        entryService.createEntry(request);
    }

    @PutMapping("/update/{entry_id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void updateEntry(@PathVariable Long entry_id, @RequestBody UpdateEntryRequest request){
        entryService.updateEntry(entry_id, request);
    }

    @DeleteMapping("/cancel/{entry_id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    private void cancelEntry(@PathVariable Long entry_id){
        entryService.deleteEntry(entry_id);
    }

    @GetMapping("/find/date/{date}")
    @ResponseStatus(HttpStatus.OK)
    public List<SimpleEntryResponse> findAllByDate(@PathVariable LocalDate date){
        return entryService.findAllByDate(date);
    };

}
