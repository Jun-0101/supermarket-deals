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

   @GetMapping
    public List<SupermarketDto> getAllSupermarkets() {
        List<Supermarket> supermarkets = supermarketService.getAll();
        return supermarkets.stream().map(market -> new SupermarketDto(market.getName())).toList();
    }

    @PostMapping("/add")
    public ResponseEntity<Supermarket> addSupermarket(@RequestBody @Valid SupermarketDto supermarket){
        return ResponseEntity.status(HttpStatus.CREATED).body(supermarketService.save(supermarket));
    }

    @DeleteMapping("/delete/{id}")
    public void deleteSupermarket(@PathVariable Long id) {
        supermarketService.delete(id);
    }
}
