package com.example.supermarket_deals.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.supermarket_deals.dto.SupermarketDto;
import com.example.supermarket_deals.entity.Supermarket;
import com.example.supermarket_deals.repository.SupermarketRepository;
import com.example.supermarket_deals.exception.SupermarketNotFoundException;

import jakarta.persistence.EntityNotFoundException;

@Service
public class SupermarketService {
    @Autowired
    private SupermarketRepository supermarketRepo;

    /**
     * Save a single supermarket described by the request DTO.
     *
     * @param request data for the supermarket to store
     */
    public Supermarket save(SupermarketDto supermarketDto) {
        if (supermarketDto == null) {
            throw new IllegalArgumentException("Supermarket cannot be null");
        }

        Supermarket supermarket = new Supermarket();
        supermarket.setName(supermarketDto.getName());

        return supermarketRepo.save(supermarket);
    }

    /**
     * Remove a supermarket record by id.
     *
     * @param dealId identifier of the supermarket; must not be null
     */
    public void delete(Long supermarketId) {
        Supermarket supermarket = supermarketRepo.findById(supermarketId)
            .orElseThrow(() -> new SupermarketNotFoundException(supermarketId));

        supermarketRepo.delete(supermarket);
    }

    /**
     * Retrieve all supermarket entries.
     */
    public List<Supermarket> getAll() {
        return supermarketRepo.findAll();
    }
}
