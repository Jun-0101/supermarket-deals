package com.example.supermarket_deals.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.supermarket_deals.entity.Product;
import com.example.supermarket_deals.repository.ProductRepository;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepo;

    public Product save(Product product) {
        if (product == null) {
            throw new IllegalArgumentException("Product cannot be null");
        }
        return productRepo.save(product);
    }

    public List<Product> saveMany(List<Product> products) {
        if (products == null) {
            throw new IllegalArgumentException("Products list cannot be null");
        }
        return productRepo.saveAll(products);
    }

    public void delete(Long productId) {
        productRepo.deleteById(productId);
    }

    public List<Product> getAll() {
        return productRepo.findAll();
    }
}
