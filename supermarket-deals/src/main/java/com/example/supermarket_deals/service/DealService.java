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
import com.example.supermarket_deals.exception.DealNotFoundException;
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

    /**
     * Retrieve all deals.
     */
    public List<DealResponseDto> getAll() {
        List<Deal> deals = dealRepository.findAll();

        return deals.stream().map(
            deal -> mapper.toDto(deal)
        ).toList();
    }

    /**
     * Find deals that are valid on the given date for supermarkets whose name
     * contains the supplied term (case-insensitive).
     *
     * @param name supermarket name (substring match, not null)
     * @param date date of validity
     */
    public List<DealResponseDto> getActiveDealsBySupermarketName(String name, LocalDate date) {
        if (name == null || name.isBlank()) {
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

    /**
     * Retrieve deals valid on the specified date for products matching a name
     * fragment (case-insensitive).
     *
     * @param name substring to look for in product names
     * @param date date of validity
     */
    public List<DealResponseDto> getActiveDealsByProductName(String name, LocalDate date) {
        List<Product> products = productRepository.findByNameContainingIgnoreCase(name);

        List<Deal> deals = dealRepository.findByProductInAndValidFromLessThanEqualAndValidToGreaterThanEqual(products, date, date);

        return deals.stream().map(
            deal -> mapper.toDto(deal)
        ).toList();
    }

    /**
     * Replace all existing deals with a new list.
     *
     * @param requests new deals to store (must not be null)
     */
    @Transactional
    public List<DealResponseDto> saveDeals(List<DealRequestDto> requests) {
        if (requests == null || requests.isEmpty()) {
            throw new IllegalArgumentException("Deal list can not be null");
        }

        dealRepository.deleteAll();

        return requests.stream().map(
            request -> saveDeal(request)
        ).toList();
    }

    /**
     * Save a single deal described by the request DTO.
     *
     * @param request data for the deal to store
     */
    public DealResponseDto saveDeal(DealRequestDto request) {
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

    /**
     * Remove a deal record by id.
     *
     * @param dealId identifier of the deal; must not be null
     */
    public void delete(Long dealId) {
        Deal deal = dealRepository.findById(dealId)
            .orElseThrow(() -> new DealNotFoundException(dealId));
        
        dealRepository.delete(deal);
    }
}
