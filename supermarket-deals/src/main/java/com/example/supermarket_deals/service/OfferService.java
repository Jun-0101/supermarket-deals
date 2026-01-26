package com.example.supermarket_deals.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;
import com.example.supermarket_deals.entity.Offer;
import com.example.supermarket_deals.entity.Product;
import com.example.supermarket_deals.entity.Supermarket;
import com.example.supermarket_deals.repository.OfferRepository;
import com.example.supermarket_deals.repository.ProductRepository;
import com.example.supermarket_deals.repository.SupermarketRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OfferService {
    private final OfferRepository offerRepository;
    private final ProductRepository productRepository;
    private final SupermarketRepository supermarketRepository;

    public List<Offer> getActiveOffersBySupermarketName(String name, LocalDate date) {
        Supermarket supermarket = supermarketRepository
            .findByNameIgnoreCase(name)
            .orElseThrow(() -> new RuntimeException("Supermarket not found"));

        return offerRepository.findBySupermarketAndValidFromLessThanEqualAndValidToGreaterThanEqual(
            supermarket, date, date
        );
    }

    public List<Offer> getActiveOffersByProductName(String name, LocalDate date) {
        List<Product> products = productRepository.findByNameContainingIgnoreCase(name);

        return offerRepository.findByProductInAndValidFromLessThanEqualAndValidToGreaterThanEqual(
            products, date, date);
    }
}
