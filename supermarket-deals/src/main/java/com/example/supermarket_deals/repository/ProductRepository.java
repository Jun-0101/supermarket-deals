package com.example.supermarket_deals.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.supermarket_deals.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Long>{
    List<Product> findByNameContainingIgnoreCase(String name);
}
