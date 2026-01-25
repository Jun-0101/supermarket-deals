package com.example.supermarket_deals.repository;

import java.time.LocalDate;
import java.util.List;

import com.example.supermarket_deals.entity.Offer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OfferRepository extends JpaRepository<Offer, Long>{
    List<Offer> findByValidFromLessThanEqualAndValidToGreaterThanEqual(LocalDate start, LocalDate end);
}
