package com.example.supermarket_deals.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DealRequestDto {
    private String productName;
    private String brand;
    private String infos;
    private String supermarketName;
    private BigDecimal price;
    private LocalDate validFrom;
    private LocalDate validTo;
}
