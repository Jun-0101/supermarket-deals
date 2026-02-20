package com.example.supermarket_deals.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DealRespondDto {
    private String productName;
    private String supermarketName;
    private BigDecimal price;
    private LocalDate validFrom;
    private LocalDate validTo;
}
