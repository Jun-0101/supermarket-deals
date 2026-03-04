package com.example.supermarket_deals.dto;

import jakarta.annotation.Nonnull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductRespondDto {
    @Nonnull
    private Long id;
    @NotBlank
    private String name;
    private String brand;
    private String infos;
}
