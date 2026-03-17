package com.example.supermarket_deals.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.annotation.Nonnull;
import jakarta.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DealRequestDto {
    @NotBlank
    private String productName;
    private String brand;
    private String infos;
    @NotBlank
    private String supermarketName;
    @Nonnull
    private BigDecimal price;
    private LocalDate validFrom;
    private LocalDate validTo;
}
