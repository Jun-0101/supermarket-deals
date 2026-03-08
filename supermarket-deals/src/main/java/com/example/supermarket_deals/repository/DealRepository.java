package com.example.supermarket_deals.repository;

import java.time.LocalDate;
import java.util.List;

import com.example.supermarket_deals.entity.Deal;
import com.example.supermarket_deals.entity.Product;
import com.example.supermarket_deals.entity.Supermarket;
import org.springframework.data.jpa.repository.JpaRepository;


public interface DealRepository extends JpaRepository<Deal, Long>{
    /**
     * Find deals that are currently active for a given supermarket.
     */
    List<Deal> findBySupermarketInAndValidFromLessThanEqualAndValidToGreaterThanEqual(
        List<Supermarket> supermarkets, LocalDate start, LocalDate end);

    /**
     * Retrieve active deals that involve any product from the supplied list.
     */
    List<Deal> findByProductInAndValidFromLessThanEqualAndValidToGreaterThanEqual(
        List<Product> products, LocalDate start, LocalDate end);
}