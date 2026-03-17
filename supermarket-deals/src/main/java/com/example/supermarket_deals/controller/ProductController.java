package com.example.supermarket_deals.controller;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.supermarket_deals.dto.ProductRequestDto;
import com.example.supermarket_deals.dto.ProductResponseDto;
import com.example.supermarket_deals.service.ProductService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/products")
public class ProductController {
    @Autowired
    private ProductService productService;

    /**
     * Retrieve all product entries.
     * 
     * URL: GET /products
     * @return list of all products
     */
    @GetMapping
    public List<ProductResponseDto> getAllProducts() {
        return productService.findAll();
    }

    /**
     * Create a new product record.
     *
     * @param request product data in JSON to persist
     * @return response entity with created product
     */
    @PostMapping
    public ResponseEntity<ProductResponseDto> addProduct(@RequestBody @Valid ProductRequestDto request){
        ProductResponseDto saved = productService.save(request);
        
        return ResponseEntity.created(URI.create("/products/" + saved.getId())).body(saved);
    } 

    /**
     * Delete a product by its identifier.
     *
     * @param id id of the product to delete
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable @Valid Long id) {
        productService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
