package com.example.supermarket_deals.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import com.example.supermarket_deals.entity.*;

@DataJpaTest
public class DealRepositoryTest {
    @Autowired
    private DealRepository dealRepository;
    @Autowired
    private TestEntityManager manager;

    private Supermarket supermarket;
    private Product product;
    private LocalDate today;

    @BeforeEach
    void setUp() {
        today = LocalDate.now();
    
        supermarket = new Supermarket();
        supermarket.setName("rewe");
        manager.persist(supermarket);

        product = new Product();
        product.setName("Red Bull");
        manager.persist(product);

        Deal deal = new Deal();
        deal.setSupermarket(supermarket);
        deal.setProduct(product);
        deal.setPrice(BigDecimal.valueOf(2.29));
        deal.setValidFrom(LocalDate.now().minusDays(1));
        deal.setValidTo(LocalDate.now().plusDays(2));
        manager.persist(deal);

        manager.flush();
    }

    @Test
    void testFindBySupermarketAndValidFromLessThanEqualAndValidToGreaterThanEqual() {
        List<Deal> deals = dealRepository.findBySupermarketAndValidFromLessThanEqualAndValidToGreaterThanEqual(supermarket, today, today);
        assertEquals(1, deals.size());
        assertEquals("rewe", deals.get(0).getSupermarket().getName());
    }

    @Test
    void testFindBySupermarketAndValidFromLessThanEqualAndValidToGreaterThanEqual_emptyFound() {
        Supermarket supermarket2 = new Supermarket();
        supermarket2.setName("lidl");
        manager.persist(supermarket2);
        manager.flush();

        List<Deal> deals = dealRepository.findBySupermarketAndValidFromLessThanEqualAndValidToGreaterThanEqual(supermarket2, today, today);
        assertTrue(deals.isEmpty());
    }

    @Test
    void testFindByProductInAndValidFromLessThanEqualAndValidToGreaterThanEqual() {
        List<Deal> deals = dealRepository.findByProductInAndValidFromLessThanEqualAndValidToGreaterThanEqual(List.of(product), today, today);
        assertEquals(1, deals.size());
        assertEquals("Red Bull", deals.get(0).getProduct().getName());
    }
}
