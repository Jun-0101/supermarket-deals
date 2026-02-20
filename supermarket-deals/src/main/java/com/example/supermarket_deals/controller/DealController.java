package com.example.supermarket_deals.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.supermarket_deals.dto.*;
import com.example.supermarket_deals.entity.Deal;
import com.example.supermarket_deals.service.DealService;

@RestController
@RequestMapping("/deal")
public class DealController {

    @Autowired
    private DealService dealService;

   @GetMapping
    public List<DealRespondDto> getAllDeals() {

        List<Deal> foundDeals =  dealService.getAll();
        return foundDeals.stream().map(deal -> 
            new DealRespondDto(deal.getProduct().getName(),
            deal.getSupermarket().getName(),
            deal.getPrice(),
            deal.getValidFrom(),
            deal.getValidTo()))
            .toList();
    }

    @GetMapping("/bySupermarket")
    public List<DealRespondDto> getActiveDealsBySupermarket(
        @RequestParam String name,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
            if (date == null) date = LocalDate.now();

            List<Deal> foundDeals = dealService.getActiveDealsBySupermarketName(name, date);
            return foundDeals.stream().map(deal -> new DealRespondDto(
                deal.getProduct().getName(),
                deal.getSupermarket().getName(),
                deal.getPrice(),
                deal.getValidFrom(),
                deal.getValidTo()
            )).toList();
        }

    @GetMapping("/byProduct")
    public List<DealRespondDto> getActiveDealsByProductName(
        @RequestParam String name,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
            if (date == null) date = LocalDate.now();

            List<Deal> foundDeals = dealService.getActiveDealsByProductName(name, date);
            return foundDeals.stream().map(deal -> new DealRespondDto(
                deal.getProduct().getName(),
                deal.getSupermarket().getName(),
                deal.getPrice(),
                deal.getValidFrom(),
                deal.getValidTo()
            )).toList();
        }

    @PostMapping("/addMany")
    public ResponseEntity<List<Deal>> saveDeals(@RequestBody List<DealRequestDto> dealRequests) {
        return ResponseEntity.status(HttpStatus.CREATED).body(dealService.saveDeals(dealRequests));
    }

    @PostMapping("/add")
    public ResponseEntity<Deal> saveDeal(@RequestBody DealRequestDto dealRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(dealService.saveDeal(dealRequest));
    }

    @DeleteMapping("/delete/{id}")
    public void deleteDeal(@PathVariable Long id) {
        dealService.delete(id);
    }
}
