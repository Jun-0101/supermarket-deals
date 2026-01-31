package com.example.supermarket_deals.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;
import com.example.supermarket_deals.entity.Deal;
import com.example.supermarket_deals.entity.Product;
import com.example.supermarket_deals.entity.Supermarket;
import com.example.supermarket_deals.repository.DealRepository;
import com.example.supermarket_deals.repository.ProductRepository;
import com.example.supermarket_deals.repository.SupermarketRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DealService {
    private final DealRepository offerRepository;
    private final ProductRepository productRepository;
    private final SupermarketRepository supermarketRepository;

    public List<Deal> getActiveDealsBySupermarketName(String name, LocalDate date) {
        Supermarket supermarket = supermarketRepository
            .findByNameIgnoreCase(name)
            .orElseThrow(() -> new RuntimeException("Supermarket not found"));

        return offerRepository.findBySupermarketAndValidFromLessThanEqualAndValidToGreaterThanEqual(
            supermarket, date, date
        );
    }

    public List<Deal> getActiveDealsByProductName(String name, LocalDate date) {
        List<Product> products = productRepository.findByNameContainingIgnoreCase(name);

        return offerRepository.findByProductInAndValidFromLessThanEqualAndValidToGreaterThanEqual(
            products, date, date);
    }
}
