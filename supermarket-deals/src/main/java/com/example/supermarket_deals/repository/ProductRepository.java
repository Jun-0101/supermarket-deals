package com.example.supermarket_deals.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.supermarket_deals.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Long>{
    /**
     * Search for products whose name contains the given term, ignoring case.
     * 
     * @param name substring to search within product names
     */
    List<Product> findByNameContainingIgnoreCase(String name);

    /**
     * Search for a product by its exact name and brand.
     *
     * @param name  the product name to match
     * @param brand the brand name to match
     */
    Optional<Product> findByNameAndBrand(String name, String brand);
}
