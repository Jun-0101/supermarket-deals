package com.example.supermarket_deals.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.supermarket_deals.dto.ProductRequestDto;
import com.example.supermarket_deals.dto.ProductResponseDto;
import com.example.supermarket_deals.entity.Product;
import com.example.supermarket_deals.exception.ProductNotFoundException;
import com.example.supermarket_deals.mapper.ProductMapper;
import com.example.supermarket_deals.repository.ProductRepository;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepo;
    @Autowired
    private ProductMapper mapper;

    /**
     * Retrieve all product entries.
     */
    @Transactional(readOnly = true)
    public List<ProductResponseDto> findAll() {
        List<Product> products = productRepo.findAll();

        return products.stream()
        .map(mapper::toDto)
        .toList();
    }
    
    /**
     * Save a single product described by the request DTO.
     *
     * @param request data for the product to store
     */
    @Transactional
    public ProductResponseDto save(ProductRequestDto request) {
        Product product = mapper.toEntity(request);
        Product saved = productRepo.save(product);

        return mapper.toDto(saved);
    }

    /**
     * Remove a product record by id.
     *
     * @param productId identifier of the product; must not be null
     */
    @Transactional
    public void delete(Long productId) {
        Product product = productRepo.findById(productId)
            .orElseThrow(() -> new ProductNotFoundException(productId));

        productRepo.delete(product);
    }
}
