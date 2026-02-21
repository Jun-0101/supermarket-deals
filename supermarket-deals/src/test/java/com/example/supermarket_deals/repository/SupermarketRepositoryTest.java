package com.example.supermarket_deals.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import com.example.supermarket_deals.entity.Supermarket;

@DataJpaTest
public class SupermarketRepositoryTest {
    @Autowired
    private SupermarketRepository supermarketRepository;
    @Autowired
    private TestEntityManager manager;

    @BeforeEach
    void setUp() {
        Supermarket supermarket1 = new Supermarket();
        supermarket1.setName("rewe");
        manager.persist(supermarket1);

        Supermarket supermarket2 = new Supermarket();
        supermarket2.setName("lidl");
        manager.persist(supermarket2);
    }

    @Test
    void testFindByNameIgnoreCase() {
        Supermarket supermarket = supermarketRepository.findByNameIgnoreCase("RewE").orElseThrow();
        assertEquals("rewe", supermarket.getName());
    }

    @Test
    void testFindByNameIgnoreCase_notFound() {
        Optional<Supermarket> supermarket = supermarketRepository.findByNameIgnoreCase("unknown");
        assertTrue(supermarket.isEmpty());
    }
}
