package com.example.supermarket_deals.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.supermarket_deals.dto.ProductRequestDto;
import com.example.supermarket_deals.dto.ProductResponseDto;
import com.example.supermarket_deals.entity.Product;
import com.example.supermarket_deals.mapper.ProductMapper;
import com.example.supermarket_deals.repository.ProductRepository;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepo;
    @Autowired
    private ProductMapper mapper;

    /**
     * Save a single product described by the request DTO.
     *
     * @param request data for the product to store
     */
    public ProductResponseDto save(ProductRequestDto request) {
        Product product = mapper.toEntity(request);
        Product saved = productRepo.save(product);

        return mapper.toDto(saved);
    }

    /**
     * Remove a product record by id.
     *
     * @param dealId identifier of the product; must not be null
     */
    public void delete(Long productId) {
        if (productId == null) {
            throw new IllegalArgumentException("Product ID must not be null");
        }
        
        productRepo.deleteById(productId);
    }

    /**
     * Retrieve all product entries.
     */
    public List<ProductResponseDto> getAll() {
        List<Product> products = productRepo.findAll();

        return products.stream().map(
            prod -> mapper.toDto(prod)
        ).toList();
    }
}
