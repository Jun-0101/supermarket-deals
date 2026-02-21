package com.example.supermarket_deals.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.supermarket_deals.dto.DealRequestDto;
import com.example.supermarket_deals.entity.*;
import com.example.supermarket_deals.repository.*;

@ExtendWith(MockitoExtension.class)
public class DealServiceTest {
    @Mock
    private DealRepository dealRepository;
    @Mock
    private ProductRepository productRepository;
    @Mock
    private SupermarketRepository supermarketRepository;
    @InjectMocks
    private DealService dealService;

    // ----------------------------
    // getActiveDealsBySupermarketName
    // ----------------------------

    @Test
    void testGetActiveDealsBySupermarketName_returnDeals() {
        Supermarket supermarket = new Supermarket();
        LocalDate date = LocalDate.now();

        when(supermarketRepository.findByNameIgnoreCase("rewe")).thenReturn(Optional.of(supermarket));
        when(dealRepository.findBySupermarketAndValidFromLessThanEqualAndValidToGreaterThanEqual(supermarket, date, date)).thenReturn(List.of(new Deal()));

        List<Deal> foundDeals = dealService.getActiveDealsBySupermarketName("rewe", date);

        assertEquals(1, foundDeals.size());
        verify(supermarketRepository).findByNameIgnoreCase("rewe");
        verify(dealRepository).findBySupermarketAndValidFromLessThanEqualAndValidToGreaterThanEqual(supermarket, date, date);
    }

    @Test
    void testGetActiveDealsBySupermarketName_throwException() {
        LocalDate date = LocalDate.now();

        when(supermarketRepository.findByNameIgnoreCase("unknown")).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> dealService.getActiveDealsBySupermarketName("unknown", date));
    }

    // ----------------------------
    // saveDeal
    // ----------------------------

    @Test
    void testSaveDeal_successfully() {
        DealRequestDto request = new DealRequestDto(1L, 3L);
        Product product = new Product();
        Supermarket supermarket = new Supermarket();

        when(productRepository.findById(request.getProductId())).thenReturn(Optional.of(product));
        when(supermarketRepository.findById(request.getSupermarketId())).thenReturn(Optional.of(supermarket));

        Deal savedDeal = dealService.saveDeal(request);
        assertNotNull(savedDeal);
        assertEquals(supermarket, savedDeal.getSupermarket());
        assertEquals(product, savedDeal.getProduct());
    }

    @Test
    void testSaveDeal_throwException() {
        DealRequestDto request = new DealRequestDto(1L, 3L);

        when(productRepository.findById(request.getProductId())).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> dealService.saveDeal(request));
    }

    // ----------------------------
    // delete
    // ----------------------------

    @Test
    void testDelete_successfully() {
        dealService.delete(1L);
        verify(dealRepository).deleteById(1L);
    }
}
