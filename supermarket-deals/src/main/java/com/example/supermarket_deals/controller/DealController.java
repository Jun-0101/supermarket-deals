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
@RequestMapping("/deals")
public class DealController {
    @Autowired
    private DealService dealService;

    /**
     * Returns all deals stored in the system
     * regardless of supermarket or date.
     *
     * URL: GET /deals
     */
   @GetMapping
    public List<DealResponseDto> getAllDeals() {
        return dealService.findAll();
    }

    /**
     * Returns active deals for a specific supermarket.
     *
     * Example requests:
     * GET /deals/by-supermarket?name=rewe
     * GET /deals/by-supermarket?name=rewe&date=2026-03-08
     * 
     * URL: GET /deals/by-supermarket
     */
    @GetMapping("/by-supermarket")
    public List<DealResponseDto> getActiveDealsBySupermarket(
        @RequestParam String name,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
            LocalDate validDate = date != null? date : LocalDate.now();

            return dealService.getActiveDealsBySupermarketName(name, validDate);
        }
    
    /**
     * Returns active deals for a specific product name.
     *
     * Example requests:
     * GET /deals/by-product?name=milch
     * GET /deals/by-product?name=milch&date=2026-03-08
     * 
     * URL: GET /deals/by-product
     */
    @GetMapping("/by-product")
    public List<DealResponseDto> getActiveDealsByProductName(
        @RequestParam String name,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
            LocalDate validDate = date != null? date : LocalDate.now();

            return dealService.getActiveDealsByProductName(name, validDate);
        }

    /**
     * Persist a single deal record.
     *
     * @param request the deal details in JSON to store
     * @return a response entity containing the saved deal and headers
     */
    @PostMapping
    public ResponseEntity<DealResponseDto> saveDeal(@RequestBody @Valid DealRequestDto request) {
        DealResponseDto saved = dealService.saveDeal(request);
        
        return ResponseEntity.created(URI.create("/deals/" + saved.getId())).body(saved);
    }

    /**
     * Persist multiple deals in a single request.
     *
     * @param requests list of deal data objects in JSON to store
     * @return response containing saved deals
     */
    @PostMapping("/bulk")
    public ResponseEntity<List<DealResponseDto>> saveDeals(@RequestBody List<@Valid DealRequestDto> requests) {
        return ResponseEntity.status(HttpStatus.CREATED).body(dealService.saveDeals(requests));
    }

    /**
     * Remove a deal by its identifier.
     *
     * @param id the deal id to delete
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDeal(@PathVariable Long id) {
        dealService.delete(id);

        return ResponseEntity.noContent().build();
    }
}
