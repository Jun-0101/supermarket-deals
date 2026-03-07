package com.example.supermarket_deals.mapper;
import org.springframework.stereotype.Component;

import com.example.supermarket_deals.dto.DealRequestDto;
import com.example.supermarket_deals.dto.DealRespondDto;
import com.example.supermarket_deals.entity.Deal;
import com.example.supermarket_deals.entity.Product;
import com.example.supermarket_deals.entity.Supermarket;

@Component
public class DealMapper {
    public DealRespondDto toDto(Deal deal) {
        return new DealRespondDto(
            deal.getId(),
            deal.getProduct().getName(),
            deal.getProduct().getBrand(),
            deal.getProduct().getInfos(),
            deal.getSupermarket().getName(),
            deal.getPrice(),
            deal.getValidFrom(),
            deal.getValidTo());
    }

    public Deal toEntity(DealRequestDto request, Product product, Supermarket supermarket) {
        return Deal.builder()
            .product(product)
            .supermarket(supermarket)
            .price(request.getPrice())
            .validFrom(request.getValidFrom())
            .validTo(request.getValidTo())
            .build();
    }
}
