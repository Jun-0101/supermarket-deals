package com.example.supermarket_deals.service;

import java.util.List;

import org.springframework.stereotype.Service;
import com.example.supermarket_deals.entity.Product;
import com.example.supermarket_deals.repository.ProductRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;

    public List<Product> searchProducts(String name) {
        return productRepository.findByNameContainingIgnoreCase(name);
    }
}
