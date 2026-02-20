package com.example.supermarket_deals.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.annotation.Nonnull;
import lombok.Data;

@Data
public class DealRequestDto {
    @Nonnull
    private Long productId;
    @Nonnull
    private Long supermarketId;
    private BigDecimal price;
    private LocalDate validFrom;
    private LocalDate validTo;
}
