package com.example.supermarket_deals.repository;

import java.time.LocalDate;
import java.util.List;

import com.example.supermarket_deals.entity.Deal;
import com.example.supermarket_deals.entity.Product;
import com.example.supermarket_deals.entity.Supermarket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface DealRepository extends JpaRepository<Deal, Long>{
    List<Deal> findBySupermarketAndValidFromLessThanEqualAndValidToGreaterThanEqual(
        // find all active deals in the given supermarket
        Supermarket supermarket, LocalDate start, LocalDate end);

    List<Deal> findByProductInAndValidFromLessThanEqualAndValidToGreaterThanEqual(
        // find all active deals, whose product names contain given name
        List<Product> products, LocalDate start, LocalDate end);

    @Modifying
    @Query("DELETE FROM Deal d WHERE d.validTo < CURRENT_DATE")
    void deleteExpiredDeals();
}
