package com.example.supermarket_deals.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.supermarket_deals.entity.Supermarket;
import com.example.supermarket_deals.service.SupermarketService;

@RestController
@RequestMapping("/supermarket")
public class SupermarketController {

    @Autowired
    private SupermarketService supermarketService;

   @GetMapping
    public List<Supermarket> getAllSupermarkets() {
        return supermarketService.getAll();
    }

    @PostMapping("/add")
    public ResponseEntity<Supermarket> addSupermarket(@RequestBody Supermarket supermarket){
        return ResponseEntity.ok(supermarketService.save(supermarket));
    } 

    @PostMapping("/addMany")
    public ResponseEntity<List<Supermarket>> addSupermarkets(@RequestBody List<Supermarket> supermarkets){
        return ResponseEntity.ok(supermarketService.saveMany(supermarkets));
    } 

    @DeleteMapping("/delete/{id}")
    public void deleteSupermarket(@PathVariable Long id) {
        supermarketService.delete(id);
    }
}
