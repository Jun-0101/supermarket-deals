package com.example.supermarket_deals.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.supermarket_deals.entity.Product;
import com.example.supermarket_deals.repository.ProductRepository;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {
    @Mock
    private ProductRepository productRepository;
    @InjectMocks
    private ProductService productService;

    // ----------------------------
    // save
    // ----------------------------

    @Test
    void testSave_successfully() {
        Product product = new Product();
        when(productRepository.save(product)).thenReturn(product);

        Product savedProduct = productService.save(product);
        assertEquals(product, savedProduct);
        verify(productRepository).save(product);
    }

    @Test
    void testSave_throwException() {
        assertThrows(IllegalArgumentException.class, () -> productService.save(null));
        verify(productRepository, never()).save(any());
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
        List<Product> prods = List.of(new Product(), new Product());
        when (productRepository.findAll()).thenReturn(prods);

        List<Product> products = productService.getAll();
        assertEquals(prods, products);
        verify(productRepository).findAll();
    }
}
