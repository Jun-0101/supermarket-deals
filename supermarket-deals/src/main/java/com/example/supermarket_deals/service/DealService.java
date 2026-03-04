package com.example.supermarket_deals.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.supermarket_deals.dto.*;
import com.example.supermarket_deals.entity.Deal;
import com.example.supermarket_deals.entity.Product;
import com.example.supermarket_deals.entity.Supermarket;
import com.example.supermarket_deals.mapper.DealMapper;
import com.example.supermarket_deals.repository.*;

@Service
public class DealService {
    @Autowired
    private DealRepository dealRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private SupermarketRepository supermarketRepository;
    @Autowired
    private DealMapper mapper;

    public List<DealRespondDto> getAll() {
        List<Deal> deals = dealRepository.findAll();

        return deals.stream().map(
            deal -> mapper.toDto(deal)
        ).toList();
    }

    public List<DealRespondDto> getActiveDealsBySupermarketName(String name, LocalDate date) {
        Supermarket supermarket = supermarketRepository
            .findByNameIgnoreCase(name)
            .orElseThrow(() -> new RuntimeException("Supermarket not found"));

        List<Deal> deals = dealRepository.findBySupermarketAndValidFromLessThanEqualAndValidToGreaterThanEqual(
            supermarket, date, date
        );

        return deals.stream().map(
            deal -> mapper.toDto(deal)
        ).toList();
    }

    public List<DealRespondDto> getActiveDealsByProductName(String name, LocalDate date) {
        List<Product> products = productRepository.findByNameContainingIgnoreCase(name);

        List<Deal> deals = dealRepository.findByProductInAndValidFromLessThanEqualAndValidToGreaterThanEqual(
            products, date, date);

        return deals.stream().map(
            deal -> mapper.toDto(deal)
        ).toList();
    }

    public List<DealRespondDto> saveDeals(List<DealRequestDto> requests) {
        if (requests == null) {
            throw new IllegalArgumentException("Deal list can not be null");
        }

        return requests.stream().map(
            request -> saveDeal(request)
        ).toList();
    }

    public DealRespondDto saveDeal(DealRequestDto request) {
        if (request == null) {
            throw new IllegalArgumentException("Request can not be null");
        }
        Long productId = request.getProductId();
        Long supermarketId = request.getSupermarketId();
        if (productId == null || supermarketId == null) {
            throw new IllegalArgumentException("Id can not be null");
        }
        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new RuntimeException("Product not found"));
        Supermarket supermarket = supermarketRepository.findById(supermarketId)
            .orElseThrow(() -> new RuntimeException("Supermarket not found"));

        Deal deal = mapper.toEntity(request, product, supermarket);

        Deal saved = dealRepository.save(deal);

        return mapper.toDto(saved);
    }

    public void delete(Long dealId) {
        if (dealId == null) {
            throw new IllegalArgumentException("Deal ID must not be null");
        }
        dealRepository.deleteById(dealId);
    }
}
