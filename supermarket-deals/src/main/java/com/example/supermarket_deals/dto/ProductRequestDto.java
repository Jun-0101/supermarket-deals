package com.example.supermarket_deals.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import jakarta.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
public class ProductRequestDto {
    @NotBlank
    private String name;
    private String brand;
    private String infos;
}
