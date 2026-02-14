package com.example.supermarket_deals.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.supermarket_deals.dto.DealRequest;
import com.example.supermarket_deals.entity.Deal;
import com.example.supermarket_deals.service.DealService;

@RestController
@RequestMapping("/deal")
public class DealController {

    @Autowired
    private DealService dealService;

   @GetMapping
    public List<Deal> getAllDeals() {
        return dealService.getAll();
    }

    @GetMapping("/bySupermarket")
    public List<Deal> getActiveDealsBySupermarket(
        @RequestParam String name,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
            if (date == null) date = LocalDate.now();

            return dealService.getActiveDealsBySupermarketName(name, date);
        }

    @GetMapping("/byProduct")
    public List<Deal> getActiveDealsByProductName(
        @RequestParam String name,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
            if (date == null) date = LocalDate.now();

            return dealService.getActiveDealsByProductName(name, date);
        }

    @PostMapping("/addMany")
    public ResponseEntity<List<Deal>> saveDeals(@RequestBody List<DealRequest> dealRequests) {
        return ResponseEntity.status(HttpStatus.CREATED).body(dealService.saveDeals(dealRequests));
    }

    @PostMapping("/add")
    public ResponseEntity<Deal> saveDeal(@RequestBody DealRequest dealRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(dealService.saveDeal(dealRequest));
    }

    @DeleteMapping("/delete/{id}")
    public void deleteDeal(@PathVariable Long id) {
        dealService.delete(id);
    }
}
