package com.example.supermarket_deals.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.supermarket_deals.dto.SupermarketDto;
import com.example.supermarket_deals.entity.Supermarket;
import com.example.supermarket_deals.service.SupermarketService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/supermarket")
public class SupermarketController {
    @Autowired
    private SupermarketService supermarketService;

    /**
     * Retrieve all supermarket entries.
     * 
     * URL: GET /supermarket
     * @return list of all supermarket
     */
   @GetMapping
    public List<SupermarketDto> getAllSupermarkets() {
        List<Supermarket> supermarkets = supermarketService.getAll();
        return supermarkets.stream().map(market -> new SupermarketDto(market.getName())).toList();
    }

    /**
     * Create a new supermarket record.
     *
     * @param request supermarket data in JSON to persist
     * @return response entity with created supermarket
     */
    @PostMapping("/add")
    public ResponseEntity<Supermarket> addSupermarket(@RequestBody @Valid SupermarketDto supermarket){
        return ResponseEntity.status(HttpStatus.CREATED).body(supermarketService.save(supermarket));
    }

    /**
     * Delete a supermarket by its identifier.
     *
     * @param id id of the supermarket to delete
     */
    @DeleteMapping("/delete/{id}")
    public void deleteSupermarket(@PathVariable Long id) {
        supermarketService.delete(id);
    }
}
