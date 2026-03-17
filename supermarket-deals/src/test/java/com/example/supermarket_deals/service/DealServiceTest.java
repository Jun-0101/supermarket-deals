package com.example.supermarket_deals.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.supermarket_deals.dto.DealRequestDto;
import com.example.supermarket_deals.dto.DealResponseDto;
import com.example.supermarket_deals.entity.Deal;
import com.example.supermarket_deals.entity.Product;
import com.example.supermarket_deals.entity.Supermarket;
import com.example.supermarket_deals.exception.DealNotFoundException;
import com.example.supermarket_deals.mapper.DealMapper;
import com.example.supermarket_deals.repository.*;

@ExtendWith(MockitoExtension.class)
public class DealServiceTest {
    @Mock
    private DealRepository dealRepository;
    @Mock
    private ProductRepository productRepository;
    @Mock
    private SupermarketRepository supermarketRepository;
    @Mock
    private DealMapper mapper;
    @InjectMocks
    private DealService dealService;

    private Deal deal;
    private DealRequestDto request;
    private DealResponseDto respond;

    @BeforeEach
    void setUp() {
        LocalDate from = LocalDate.now().minusDays(1);
        LocalDate to = LocalDate.now().plusDays(1);
        BigDecimal price = BigDecimal.valueOf(1.99);

        Product product = new Product();
        product.setName("Drink");
        product.setBrand("Red Bull");
        product.setInfos("200 ml");

        Supermarket supermarket = new Supermarket();
        supermarket.setName("rewe");
        deal = Deal.builder()
            .product(product)
            .supermarket(supermarket)
            .price(price)
            .validFrom(from)
            .validTo(to)
            .build();

        request = new DealRequestDto("Drink", "Red Bull", "200ml","rewe", price, from, to);
        respond = new DealResponseDto(1L, "Drink", "Red Bull", "200ml", "rewe", price, from, to);
    }
    // ----------------------------
    // getActiveDealsBySupermarketName
    // ----------------------------

    @Test
    void testGetActiveDealsBySupermarketName_returnDealDto() {
        Supermarket supermarket = deal.getSupermarket();
        LocalDate date = LocalDate.now();

        when(supermarketRepository.findByNameContainingIgnoreCase("rewe")).thenReturn(List.of(supermarket));
        when(dealRepository.findBySupermarketInAndValidFromLessThanEqualAndValidToGreaterThanEqual(List.of(supermarket), date, date)).thenReturn(List.of(deal));
        when(mapper.toDto(deal)).thenReturn(respond);

        List<DealResponseDto> foundDeals = dealService.getActiveDealsBySupermarketName("rewe", date);

        assertEquals(1, foundDeals.size());
        assertEquals(respond, foundDeals.get(0));
        verify(supermarketRepository).findByNameContainingIgnoreCase("rewe");
        verify(dealRepository).findBySupermarketInAndValidFromLessThanEqualAndValidToGreaterThanEqual(List.of(supermarket), date, date);
    }

    @Test
    void testGetActiveDealsBySupermarketName_throwException() {
        LocalDate date = LocalDate.now();

        when(supermarketRepository.findByNameContainingIgnoreCase("unknown")).thenReturn(List.of());

        assertThrows(RuntimeException.class, () -> dealService.getActiveDealsBySupermarketName("unknown", date));
    }

    // ----------------------------
    // saveDeal
    // ----------------------------

    @Test
    void testSaveDeal_successfully() {
        Product product = deal.getProduct();
        Supermarket supermarket = deal.getSupermarket();

        when(productRepository.findByNameAndBrand(request.getProductName(), request.getBrand())).thenReturn(Optional.of(product));
        when(supermarketRepository.findByNameIgnoreCase(request.getSupermarketName())).thenReturn(Optional.of(supermarket));
        when(mapper.toEntity(request, product, supermarket)).thenReturn(deal);
        when(dealRepository.save(any())).thenReturn(deal);
        when(mapper.toDto(deal)).thenReturn(respond);

        DealResponseDto savedDeal = dealService.saveDeal(request);
        assertEquals(respond, savedDeal);
        verify(dealRepository).save(deal);
    }

    // ----------------------------
    // delete
    // ----------------------------

    @Test
    void testDelete_successfully() {
        when(dealRepository.findById(1L)).thenReturn(Optional.of(deal));
        dealService.delete(1L);

        verify(dealRepository).findById(1L);
        verify(dealRepository).delete(deal);
    }

    @Test
    void testDelete_notFound() {
        Long id = 1L;
        when(dealRepository.findById(1L)).thenReturn(Optional.empty());

        DealNotFoundException ex = assertThrows(DealNotFoundException.class, () -> dealService.delete(id));
        assertEquals("Deal with id 1 not found", ex.getMessage());

        verify(dealRepository).findById(id);
        verify(dealRepository, never()).delete(any());
    }
}
