package com.example.supermarket_deals.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import jakarta.annotation.Nonnull;
import jakarta.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
public class DealRespondDto {
    @Nonnull
    private Long id;
    @NotBlank
    private String productName;
    @NotBlank
    private String supermarketName;
    private BigDecimal price;
    private LocalDate validFrom;
    private LocalDate validTo;
}
