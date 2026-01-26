package com.example.supermarket_deals.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.supermarket_deals.entity.Supermarket;

public interface SupermarketRepository extends JpaRepository<Supermarket, Long>{
    Optional<Supermarket> findByNameIgnoreCase(String name);
}
