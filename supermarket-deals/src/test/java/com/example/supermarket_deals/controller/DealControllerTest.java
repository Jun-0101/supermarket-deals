package com.example.supermarket_deals.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.example.supermarket_deals.dto.DealRequestDto;
import com.example.supermarket_deals.dto.DealResponseDto;
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

    private DealRequestDto request;
    private DealResponseDto respond;

    @BeforeEach
    void setUp() {
        LocalDate from = LocalDate.now().minusDays(1);
        LocalDate to = LocalDate.now().plusDays(1);
        BigDecimal price = BigDecimal.valueOf(1.99);

        request = new DealRequestDto("Drink", "Red Bull", "200ml","rewe", price, from, to);
        respond = new DealResponseDto(1L, "Drink", "Red Bull", "200ml", "rewe", price, from, to);
    }

    // -------------------------
    // GET /deals
    // -------------------------
    @Test
    void testGetAllDeals() throws Exception {
        when(dealService.getAll()).thenReturn(List.of(respond));

        mockMvc.perform(get("/deals"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].productName").value("Drink"))
            .andExpect(jsonPath("$[0].supermarketName").value("rewe"));
        
            verify(dealService, times(1)).getAll();
    }

    // -------------------------
    // GET /deals/by-supermarket?name
    // -------------------------
    @Test
    void testGetActiveDealsBySupermarket() throws Exception {
        when(dealService.getActiveDealsBySupermarketName("rewe", LocalDate.now())).thenReturn(List.of(respond));

        mockMvc.perform(get("/deals/by-supermarket").param("name", "rewe"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].productName").value("Drink"))
            .andExpect(jsonPath("$[0].price").value("1.99"))
            .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void testGetActiveDealsBySupermarket_notFound() throws Exception {
        when(dealService.getActiveDealsBySupermarketName("rewe", LocalDate.now())).thenReturn(List.of());

        mockMvc.perform(get("/deals/by-supermarket").param("name", "rewe"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.length()").value(0));
    }

    // -------------------------
    // GET /deals/by-product?name
    // -------------------------
    @Test
    void testGetActiveDealsByProductName() throws Exception {
        when(dealService.getActiveDealsByProductName("Red Bull", LocalDate.now())).thenReturn(List.of(respond));

        mockMvc.perform(get("/deals/by-product").param("name", "Red Bull"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].supermarketName").value("rewe"))
            .andExpect(jsonPath("$[0].price").value("1.99"));
    }

    // -------------------------
    // POST /deals
    // -------------------------
    @Test
    void testSaveDeal() throws Exception {
        when(dealService.saveDeal(any(DealRequestDto.class))).thenReturn(respond);

        mockMvc.perform(post("/deals")
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .content(mapper.writeValueAsString(request)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.price").value(1.99));

        verify(dealService, times(1)).saveDeal(any());
    }
}
