package com.example.supermarket_deals.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.supermarket_deals.entity.Supermarket;

public interface SupermarketRepository extends JpaRepository<Supermarket, Long>{
    /**
     * Search for a supermarket with its exact name, ignoring case.
     * @param name fragment to search for
     */
    Optional<Supermarket> findByNameIgnoreCase(String name);

    /**
     * Search for supermarkets whose name contains the given term, ignoring case.
     * @param name fragment to search for
     */
    List<Supermarket> findByNameContainingIgnoreCase(String name);
}
