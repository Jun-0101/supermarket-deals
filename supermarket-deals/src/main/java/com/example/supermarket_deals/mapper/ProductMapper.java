package com.example.supermarket_deals.mapper;

import org.springframework.stereotype.Component;

import com.example.supermarket_deals.dto.ProductRequestDto;
import com.example.supermarket_deals.dto.ProductResponseDto;
import com.example.supermarket_deals.entity.Product;

@Component
public class ProductMapper {
    public ProductResponseDto toDto(Product product) {
        return new ProductResponseDto(
            product.getId(),
            product.getName(),
            product.getBrand(),
            product.getInfos());
    }

    public Product toEntity(ProductRequestDto respond) {
        return Product.builder()
        .name(respond.getName())
        .brand(respond.getBrand())
        .infos(respond.getInfos())
        .build();
    }
}
