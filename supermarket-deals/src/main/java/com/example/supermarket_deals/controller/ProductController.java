package com.example.supermarket_deals.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.supermarket_deals.dto.ProductDto;
import com.example.supermarket_deals.entity.Product;
import com.example.supermarket_deals.service.ProductService;

@RestController
@RequestMapping("/product")
public class ProductController {
    @Autowired
    private ProductService productService;

    @GetMapping
    public List<ProductDto> getAllProducts() {
        List<Product> products = productService.getAll();
        return products.stream().map(prod -> new ProductDto(
            prod.getName(),
            prod.getBrand(),
            prod.getInfos()
            )).toList();
    }

    @PostMapping("/add")
    public ResponseEntity<Product> addProduct(@RequestBody Product product){
        return ResponseEntity.ok(productService.save(product));
    } 

    @PostMapping("/addMany")
    public ResponseEntity<List<Product>> addProducts(@RequestBody List<Product> products){
        return ResponseEntity.ok(productService.saveMany(products));
    }

    @DeleteMapping("/delete/{id}")
    public void deleteProduct(@PathVariable Long id) {
        productService.delete(id);
    }
}
