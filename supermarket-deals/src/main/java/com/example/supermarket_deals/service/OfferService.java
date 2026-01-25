package com.example.supermarket_deals.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;
import com.example.supermarket_deals.entity.Offer;
import com.example.supermarket_deals.entity.Supermarket;
import com.example.supermarket_deals.repository.OfferRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OfferService {
    private final OfferRepository offerRepository;

    public List<Offer> getActiveOffersForSupermarket(Supermarket supermarket, LocalDate date) {
        return offerRepository.findByValidFromLessThanEqualAndValidToGreaterThanEqualAndSupermarket(
            date, date, supermarket
        );
    }
}
