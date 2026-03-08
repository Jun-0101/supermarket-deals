package com.example.supermarket_deals.repository;

import java.time.LocalDate;
import java.util.List;

import com.example.supermarket_deals.entity.Deal;
import com.example.supermarket_deals.entity.Product;
import com.example.supermarket_deals.entity.Supermarket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface DealRepository extends JpaRepository<Deal, Long>{
    /**
     * Find deals that are currently active for a given supermarket.
     *
     * @param supermarket the supermarket to filter by
     * @param date the date to check validity against
     * @return list of active deals for the supermarket on the given date
     */
    @Query("""
        SELECT d FROM Deal d
        WHERE d.supermarket = :supermarket
        AND d.validFrom <= :date
        AND d.validTo >= :date
    """)
    List<Deal> findActiveDealsBySupermarket(
            @Param("supermarket") Supermarket supermarket,
            @Param("date") LocalDate date);


    /**
     * Retrieve active deals that involve any product from the supplied list.
     *
     * @param products list of products to search deals for
     * @param date the date used to determine if a deal is active
     * @return list of matching active deals
     */
    @Query("""
        SELECT d FROM Deal d
        WHERE d.product in :products
        AND d.validFrom <= :date
        AND d.validTo >= :date
    """)
    List<Deal> findActiveDealsByProducList(
            @Param("supermarket") List<Product> products,
            @Param("date") LocalDate date);
    }
