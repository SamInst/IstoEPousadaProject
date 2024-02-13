package com.example.PousadaIstoE.controllers;

import com.example.PousadaIstoE.request.EntryRequest;
import com.example.PousadaIstoE.request.UpdateEntryRequest;
import com.example.PousadaIstoE.response.EntryResponse;
import com.example.PousadaIstoE.response.SimpleEntryResponse;
import com.example.PousadaIstoE.services.EntryService2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/entrys")
public class EntryController2 {
    private final EntryService2 entryService2;

    public EntryController2(EntryService2 entryService2) {
        this.entryService2 = entryService2;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Page<SimpleEntryResponse> findAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return entryService2.findAll(pageable);
    }


    @GetMapping("/find_by_id/{entry_id}")
    @ResponseStatus(HttpStatus.OK)
    public EntryResponse findById(@PathVariable Long entry_id){
        return entryService2.findById(entry_id);
    }

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public void createEntry(@RequestBody EntryRequest request){
        entryService2.createEntry(request);
    }

    @PutMapping("/update/{entry_id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void updateEntry(@PathVariable Long entry_id, @RequestBody UpdateEntryRequest request){
        entryService2.updateEntry(entry_id, request);
    }



}
