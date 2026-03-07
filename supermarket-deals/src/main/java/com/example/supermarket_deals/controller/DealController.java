package com.example.supermarket_deals.controller;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import com.example.supermarket_deals.dto.*;
import com.example.supermarket_deals.service.DealService;

@RestController
@RequestMapping("/deal")
public class DealController {

    @Autowired
    private DealService dealService;

   @GetMapping
    public List<DealRespondDto> getAllDeals() {
        return dealService.getAll();
    }

    @GetMapping("/bySupermarket")
    public List<DealRespondDto> getActiveDealsBySupermarket(
        @RequestParam String name,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
            if (date == null) date = LocalDate.now();

            return dealService.getActiveDealsBySupermarketName(name, date);
        }

    @GetMapping("/byProduct")
    public List<DealRespondDto> getActiveDealsByProductName(
        @RequestParam String name,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
            if (date == null) date = LocalDate.now();

            return dealService.getActiveDealsByProductName(name, date);
        }

    @PostMapping("/add")
    public ResponseEntity<DealRespondDto> saveDeal(@RequestBody @Valid DealRequestDto request) {
        DealRespondDto saved = dealService.saveDeal(request);
        
        return ResponseEntity.created(URI.create("/product/" + saved.getId())).body(saved);
    }

    @PostMapping("/addMany")
    public ResponseEntity<List<DealRespondDto>> saveDeals(@RequestBody List<DealRequestDto> requests) {
        return ResponseEntity.status(HttpStatus.CREATED).body(dealService.saveDeals(requests));
    }

    @DeleteMapping("/delete/{id}")
    public void deleteDeal(@PathVariable Long id) {
        dealService.delete(id);
    }
}
