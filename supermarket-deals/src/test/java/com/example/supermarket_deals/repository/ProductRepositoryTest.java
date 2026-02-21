package com.example.supermarket_deals.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import com.example.supermarket_deals.entity.Product;


@DataJpaTest
public class ProductRepositoryTest {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private TestEntityManager manager;

    @BeforeEach
    void setUp() {
        Product product1 = new Product();
        product1.setName("Lindt Pralinen");
        manager.persist(product1);

        Product product2 = new Product();
        product2.setName("Red Bull");
        manager.persist(product2);

        Product product3 = new Product();
        product3.setName("Lindt Lindor");
        manager.persist(product3);

        manager.flush();
    }

    @Test
    void testFindByNameContainingIgnoreCase_successfully() {
        List<Product> products = productRepository.findByNameContainingIgnoreCase("lindt");
        assertEquals(2, products.size());

        List<Product> products2 = productRepository.findByNameContainingIgnoreCase("Red bull");
        assertEquals(1, products2.size());
    }

    @Test
    void testFindByNameContainingIgnoreCase_empty() {
        List<Product> products = productRepository.findByNameContainingIgnoreCase("Brot");
        assertEquals(0, products.size());
    }
}
