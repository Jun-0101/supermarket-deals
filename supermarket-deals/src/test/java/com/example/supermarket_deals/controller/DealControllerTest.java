package com.example.supermarket_deals.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.example.supermarket_deals.dto.DealRequestDto;
import com.example.supermarket_deals.dto.DealRespondDto;
import com.example.supermarket_deals.entity.Deal;
import com.example.supermarket_deals.entity.Product;
import com.example.supermarket_deals.entity.Supermarket;
import com.example.supermarket_deals.service.DealService;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(DealController.class)
public class DealControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper mapper;
    @MockitoBean
    private DealService dealService;

    private Deal deal;
    private DealRequestDto request;
    private DealRespondDto respond;

    @BeforeEach
    void setUp() {
        LocalDate from = LocalDate.now().minusDays(1);
        LocalDate to = LocalDate.now().plusDays(1);
        BigDecimal price = BigDecimal.valueOf(1.99);
        Product product = new Product();
        product.setName("Red Bull");
        Supermarket supermarket = new Supermarket();
        supermarket.setName("rewe");
        deal = Deal.builder()
            .product(product)
            .supermarket(supermarket)
            .price(price)
            .validFrom(from)
            .validTo(to)
            .build();

        request = new DealRequestDto(1L, 1L, price, from, to);
        respond = new DealRespondDto(1L, "Red Bull", "rewe", price, from, to);
    }

    // -------------------------
    // GET /deal
    // -------------------------
    @Test
    void testGetAllDeals() throws Exception {
        when(dealService.getAll()).thenReturn(List.of(respond));

        mockMvc.perform(get("/deal"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].productName").value("Red Bull"))
            .andExpect(jsonPath("$[0].supermarketName").value("rewe"));
    }

    // -------------------------
    // GET /deal/bySupermarket?name
    // -------------------------
    @Test
    void testGetActiveDealsBySupermarket() throws Exception {
        when(dealService.getActiveDealsBySupermarketName("rewe", LocalDate.now())).thenReturn(List.of(respond));

        mockMvc.perform(get("/deal/bySupermarket").param("name", "rewe"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].productName").value("Red Bull"))
            .andExpect(jsonPath("$[0].price").value("1.99"))
            .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void testGetActiveDealsBySupermarket_notFound() throws Exception {
        when(dealService.getActiveDealsBySupermarketName("rewe", LocalDate.now())).thenReturn(List.of());

        mockMvc.perform(get("/deal/bySupermarket").param("name", "rewe"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.length()").value(0));
    }

    // -------------------------
    // GET /deal/byProduct?name
    // -------------------------
    @Test
    void testGetActiveDealsByProductName() throws Exception {
        when(dealService.getActiveDealsByProductName("Red Bull", LocalDate.now())).thenReturn(List.of(respond));

        mockMvc.perform(get("/deal/byProduct").param("name", "Red Bull"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].supermarketName").value("rewe"))
            .andExpect(jsonPath("$[0].price").value("1.99"));
    }

    // -------------------------
    // GET /deal/add
    // -------------------------
    @Test
    void testSaveDeal() throws Exception {
        when(dealService.saveDeal(any(DealRequestDto.class))).thenReturn(respond);

        mockMvc.perform(post("/deal/add").contentType(MediaType.APPLICATION_JSON_VALUE).content(Objects.requireNonNull(mapper.writeValueAsString(request))))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.price").value(1.99));
    }
}
