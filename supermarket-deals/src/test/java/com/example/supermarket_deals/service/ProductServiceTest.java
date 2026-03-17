package com.example.supermarket_deals.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.supermarket_deals.dto.ProductRequestDto;
import com.example.supermarket_deals.dto.ProductResponseDto;
import com.example.supermarket_deals.entity.Product;
import com.example.supermarket_deals.mapper.ProductMapper;
import com.example.supermarket_deals.repository.ProductRepository;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {
    @Mock
    private ProductRepository productRepository;
    @Mock
    private ProductMapper mapper;
    @InjectMocks
    private ProductService productService;

    private ProductRequestDto requestDto;
    private ProductResponseDto respondDto;
    private Product product;

    @BeforeEach
    void setUp() {
        product = Product.builder().name("Milk").brand("brand").infos("infos").build();
        requestDto = new ProductRequestDto("Milk", "brand", "infos");
        respondDto = new ProductResponseDto(1L, "Milk", "brand", "infos");
    }

    // ----------------------------
    // save
    // ----------------------------

    @Test
    void testSave_successfully() {
        when(mapper.toEntity(requestDto)).thenReturn(product);
        when(productRepository.save(product)).thenReturn(product);
        when(mapper.toDto(product)).thenReturn(respondDto);

        ProductResponseDto respond = productService.save(requestDto);
        assertEquals(respondDto, respond);

        verify(mapper).toEntity(requestDto);
        verify(productRepository).save(product);
        verify(mapper).toDto(product);
    }

    // ----------------------------
    // delete
    // ----------------------------

    @Test
    void testDelete_successfully() {
        Long id = 1L;
        productService.delete(id);
        verify(productRepository).deleteById(id);
    }

    // ----------------------------
    // getAll
    // ----------------------------
    
    @Test
    void testGetAll() {
        List<Product> products = List.of(product);
        when (productRepository.findAll()).thenReturn(products);
        when(mapper.toDto(product)).thenReturn(respondDto);

        List<ProductResponseDto> responces = productService.getAll();
        assertEquals(List.of(respondDto), responces);
        verify(productRepository).findAll();
    }
}
