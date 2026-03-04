package com.example.supermarket_deals.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.supermarket_deals.dto.ProductRequestDto;
import com.example.supermarket_deals.dto.ProductRespondDto;
import com.example.supermarket_deals.entity.Product;
import com.example.supermarket_deals.mapper.ProductMapper;
import com.example.supermarket_deals.repository.ProductRepository;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepo;
    @Autowired
    private ProductMapper mapper;

    public ProductRespondDto save(ProductRequestDto request) {
        Product product = mapper.toEntity(request);

        Product saved = productRepo.save(product);
        return mapper.toDto(saved);
    }

    public List<ProductRespondDto> saveMany(List<ProductRequestDto> requests) {
        if (requests == null) {
            throw new IllegalArgumentException("Products list cannot be null");
        }

        List<ProductRespondDto> dtos = requests.stream().map(request -> save(request)).toList();
        return dtos;
    }

    public void delete(Long productId) {
        if (productId == null) {
            throw new IllegalArgumentException("Product ID must not be null");
        }
        productRepo.deleteById(productId);
    }

    public List<ProductRespondDto> getAll() {
        List<Product> products = productRepo.findAll();

        return products.stream().map(
            prod -> mapper.toDto(prod)
        ).toList();
    }
}
