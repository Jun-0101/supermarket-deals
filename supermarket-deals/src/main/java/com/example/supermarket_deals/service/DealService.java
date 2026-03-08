package com.example.supermarket_deals.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

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
        if (name == null) {
            throw new IllegalArgumentException("Supermarket name must not be blank");
        }

        List<Supermarket> supermarkets = supermarketRepository.findByNameContainingIgnoreCase(name);
        if (supermarkets.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Supermarket not found");
        }

        // collect active deals for all matching supermarkets
        List<Deal> deals = dealRepository.findBySupermarketInAndValidFromLessThanEqualAndValidToGreaterThanEqual(supermarkets, date, date);

        return deals.stream().map(
            deal -> mapper.toDto(deal)
        ).toList();
    }

    public List<DealRespondDto> getActiveDealsByProductName(String name, LocalDate date) {
        List<Product> products = productRepository.findByNameContainingIgnoreCase(name);

        List<Deal> deals = dealRepository.findByProductInAndValidFromLessThanEqualAndValidToGreaterThanEqual(products, date, date);

        return deals.stream().map(
            deal -> mapper.toDto(deal)
        ).toList();
    }

    @Transactional
    public List<DealRespondDto> saveDeals(List<DealRequestDto> requests) {
        if (requests == null) {
            throw new IllegalArgumentException("Deal list can not be null");
        }
        dealRepository.deleteAll();

        return requests.stream().map(
            request -> saveDeal(request)
        ).toList();
    }

    public DealRespondDto saveDeal(DealRequestDto request) {
        // find or save scrapped product
        Product product = productRepository
                .findByNameAndBrand(request.getProductName(), request.getBrand())
                .orElseGet(() -> {
                    Product p = new Product();
                    p.setName(request.getProductName());
                    p.setBrand(request.getBrand());
                    p.setInfos(request.getInfos());
                    return productRepository.save(p);
                });

        // find or save supermarket
        Supermarket supermarket = supermarketRepository
                .findByNameIgnoreCase(request.getSupermarketName())
                .orElseGet(() -> {
                    Supermarket s = Supermarket.builder()
                        .name(request.getSupermarketName())
                        .build();
                    return supermarketRepository.save(s);
                });

        // create deal
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
