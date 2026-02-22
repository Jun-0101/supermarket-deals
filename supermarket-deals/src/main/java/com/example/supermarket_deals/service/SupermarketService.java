package com.example.supermarket_deals.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.supermarket_deals.dto.SupermarketDto;
import com.example.supermarket_deals.entity.Supermarket;
import com.example.supermarket_deals.repository.SupermarketRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class SupermarketService {
    @Autowired
    private SupermarketRepository supermarketRepo;

    public Supermarket save(SupermarketDto supermarketDto) {
        if (supermarketDto == null) {
            throw new IllegalArgumentException("Supermarket cannot be null");
        }
        Supermarket supermarket = new Supermarket();
        supermarket.setName(supermarketDto.getName());

        return supermarketRepo.save(supermarket);
    }

    public List<Supermarket> saveMany(List<SupermarketDto> supermarketDtos) {
        if (supermarketDtos == null || supermarketDtos.isEmpty()) {
            throw new IllegalArgumentException("Supermarket list cannot be empty");
        }
        List<Supermarket> supermarkets = supermarketDtos.stream().map(dto -> Supermarket.builder().name(dto.getName()).build()).toList();
        return supermarketRepo.saveAll(supermarkets);
    }

    public void delete(Long supermarketId) {
        if (supermarketId == null) {
            throw new IllegalArgumentException("Supermarket ID must not be null");
        }
        if (!supermarketRepo.existsById(supermarketId)) {
            throw new EntityNotFoundException("Supermarket ID not found");
        }
        supermarketRepo.deleteById(supermarketId);
    }

    public List<Supermarket> getAll() {
        return supermarketRepo.findAll();
    }
}
