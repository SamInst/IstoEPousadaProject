package com.example.PousadaIstoE.controllers;

import com.example.PousadaIstoE.model.Entry;
import com.example.PousadaIstoE.response.EntryResponse;
import com.example.PousadaIstoE.response.SimpleEntryResponse;
import com.example.PousadaIstoE.Enums.EntryStatus;
import com.example.PousadaIstoE.services.EntryService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@RestController
@RequestMapping("/entradas")
public class EntryController {
    private final EntryService entryService;
    public EntryController(EntryService entryService) {
        this.entryService = entryService;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Page<SimpleEntryResponse> findAll(
    @RequestParam(defaultValue = "0") int page,
    @RequestParam(defaultValue = "10") int size,
    @RequestParam(defaultValue = "id") String sortField
    ) {
        Sort.Order sortOrder = Sort.Order.desc(sortField);
        Sort sort = Sort.by(sortOrder);
        Pageable pageable = PageRequest.of(page, size, sort);
        return entryService.findAll(pageable);
    }

//    @GetMapping("/{id}")
//    @ResponseStatus(HttpStatus.OK)
//    public AtomicReference<EntryResponse> findById(@PathVariable ("id") Long id){
//        return entryService.findById(id);
//    }

//    @ResponseStatus(HttpStatus.CREATED)
//    @PostMapping("/register")
//    public Entry entradas(Entry entry){
//        return entryService.registerEntrada(entry);
//    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    void atualizarEntrada (@PathVariable ("id") Long entradaID, @RequestBody Entry entry){
         entryService.updateEntradaData(entradaID, entry);
    }
    @GetMapping("/findByStatusEntrada")
    @ResponseStatus(HttpStatus.OK)
    public List<Entry> findByStatus(EntryStatus entryStatus){
        return entryService.findByStatusEntrada(entryStatus);
    }
    @GetMapping("/findEntradaHoje")
    @ResponseStatus(HttpStatus.OK)
    public List<Entry> findEntradaToday(){
        return entryService.findEntradaByToday();
    }

    @GetMapping("/findEntradaByData")
    @ResponseStatus(HttpStatus.OK)
    public List<Entry> findEntradaByData(LocalDate data_entrada) {
        return entryService.findEntradaByDate(data_entrada);
    }
}
