package com.example.supermarket_deals.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.supermarket_deals.entity.Supermarket;
import com.example.supermarket_deals.repository.SupermarketRepository;

@Service
public class SupermarketService {
    @Autowired
    private SupermarketRepository supermarketRepo;

    public Supermarket save(Supermarket supermarket) {
        if (supermarket == null) {
            throw new IllegalArgumentException("supermarket cannot be null");
        }
        return supermarketRepo.save(supermarket);
    }

    public List<Supermarket> saveMany(List<Supermarket> supermarkets) {
        if (supermarkets == null) {
            throw new IllegalArgumentException("supermarket list cannot be null");
        }
        return supermarketRepo.saveAll(supermarkets);
    }

    public void delete(Long supermarketId) {
        if (supermarketId == null) {
            throw new IllegalArgumentException("Supermarket ID must not be null");
        }
        supermarketRepo.deleteById(supermarketId);
    }

    public List<Supermarket> getAll() {
        return supermarketRepo.findAll();
    }
}
