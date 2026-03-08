package com.example.supermarket_deals.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.supermarket_deals.entity.Supermarket;

public interface SupermarketRepository extends JpaRepository<Supermarket, Long>{
    Optional<Supermarket> findByNameIgnoreCase(String name);

    /**
     * Lookup supermarkets whose name contains the supplied fragment, ignoring case.
     * @param name fragment to search for
     * @return list of matching supermarkets (possibly empty)
     */
    List<Supermarket> findByNameContainingIgnoreCase(String name);
}
