package com.example.supermarket_deals.controller;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.supermarket_deals.dto.ProductRequestDto;
import com.example.supermarket_deals.dto.ProductRespondDto;
import com.example.supermarket_deals.service.ProductService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/product")
public class ProductController {
    @Autowired
    private ProductService productService;

    /**
     * Retrieve all product entries.
     * 
     * URL: GET /product
     * @return list of all products
     */
    @GetMapping
    public List<ProductRespondDto> getAllProducts() {
        return productService.getAll();
    }

    /**
     * Create a new product record.
     *
     * @param request product data in JSON to persist
     * @return response entity with created product
     */
    @PostMapping("/add")
    public ResponseEntity<ProductRespondDto> addProduct(@RequestBody @Valid ProductRequestDto request){
        ProductRespondDto saved = productService.save(request);
        
        return ResponseEntity.created(URI.create("/product/" + saved.getId())).body(saved);
    } 

    /**
     * Delete a product by its identifier.
     *
     * @param id id of the product to delete
     */
    @DeleteMapping("/delete/{id}")
    public void deleteProduct(@PathVariable Long id) {
        productService.delete(id);
    }
}
