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

    /**
     * Returns all deals stored in the system
     * regardless of supermarket or date.
     *
     * URL: GET /deal
     */
   @GetMapping
    public List<DealRespondDto> getAllDeals() {
        return dealService.getAll();
    }

    /**
     * Returns active deals for a specific supermarket.
     *
     * Example requests:
     * GET /deal/bySupermarket?name=rewe
     * GET /deal/bySupermarket?name=rewe&date=2026-03-08
     * 
     * URL: GET /deal/bySupermarket
     */
    @GetMapping("/bySupermarket")
    public List<DealRespondDto> getActiveDealsBySupermarket(
        @RequestParam String name,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
            if (date == null) date = LocalDate.now();

            return dealService.getActiveDealsBySupermarketName(name, date);
        }
    
    /**
     * Returns active deals for a specific product name.
     *
     * Example requests:
     * GET /deal/byProduct?name=milch
     * GET /deal/byProduct?name=milch&date=2026-03-08
     * 
     * URL: GET /deal/byProduct
     */
    @GetMapping("/byProduct")
    public List<DealRespondDto> getActiveDealsByProductName(
        @RequestParam String name,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
            if (date == null) date = LocalDate.now();

            return dealService.getActiveDealsByProductName(name, date);
        }

    /**
     * Persist a single deal record.
     *
     * @param request the deal details in JSON to store
     * @return a response entity containing the saved deal and headers
     */
    @PostMapping("/add")
    public ResponseEntity<DealRespondDto> saveDeal(@RequestBody @Valid DealRequestDto request) {
        DealRespondDto saved = dealService.saveDeal(request);
        
        return ResponseEntity.created(URI.create("/product/" + saved.getId())).body(saved);
    }

    /**
     * Persist multiple deals in a single request.
     *
     * @param requests list of deal data objects in JSON to store
     * @return response containing saved deals
     */
    @PostMapping("/addMany")
    public ResponseEntity<List<DealRespondDto>> saveDeals(@RequestBody List<DealRequestDto> requests) {
        return ResponseEntity.status(HttpStatus.CREATED).body(dealService.saveDeals(requests));
    }

    /**
     * Remove a deal by its identifier.
     *
     * @param id the deal id to delete
     */
    @DeleteMapping("/delete/{id}")
    public void deleteDeal(@PathVariable Long id) {
        dealService.delete(id);
    }
}
