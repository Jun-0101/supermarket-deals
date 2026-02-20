package com.example.supermarket_deals.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.supermarket_deals.dto.*;
import com.example.supermarket_deals.entity.*;
import com.example.supermarket_deals.repository.*;

@Service
public class DealService {
    @Autowired
    private DealRepository dealRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private SupermarketRepository supermarketRepository;

    public List<Deal> getActiveDealsBySupermarketName(String name, LocalDate date) {
        Supermarket supermarket = supermarketRepository
            .findByNameIgnoreCase(name)
            .orElseThrow(() -> new RuntimeException("Supermarket not found"));

        return dealRepository.findBySupermarketAndValidFromLessThanEqualAndValidToGreaterThanEqual(
            supermarket, date, date
        );
    }

    public List<Deal> getActiveDealsByProductName(String name, LocalDate date) {
        List<Product> products = productRepository.findByNameContainingIgnoreCase(name);

        return dealRepository.findByProductInAndValidFromLessThanEqualAndValidToGreaterThanEqual(
            products, date, date);
    }

    public List<Deal> saveDeals(List<DealRequestDto> requests) {
        List<Deal> deals = requests.stream().map(req -> {
            return saveDeal(req);
        }).toList();

        if (deals == null) {
            throw new IllegalArgumentException("Deal list can not be null");
        }

        return dealRepository.saveAll(deals);
    }

    public Deal saveDeal(DealRequestDto request) {
        if (request == null) {
            throw new IllegalArgumentException("Request can not be null");
        }
        Product product = productRepository.findById(request.getProductId())
                        .orElseThrow(() -> new RuntimeException("Product not found"));
        Supermarket supermarket = supermarketRepository.findById(request.getSupermarketId())
            .orElseThrow(() -> new RuntimeException("Supermarket not found"));

        return Deal.builder()
            .product(product)
            .supermarket(supermarket)
            .price(request.getPrice())
            .validFrom(request.getValidFrom())
            .validTo(request.getValidTo())
            .build();
    }

    public void delete(Long dealId) {
        dealRepository.deleteById(dealId);
    }

    public List<Deal> getAll() {
        return dealRepository.findAll();
    }
}
