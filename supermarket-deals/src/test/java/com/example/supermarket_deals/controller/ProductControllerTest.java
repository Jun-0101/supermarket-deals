package com.example.supermarket_deals.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.example.supermarket_deals.dto.ProductRespondDto;
import com.example.supermarket_deals.service.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(ProductController.class)
public class ProductControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper mapper;
    @MockitoBean
    private ProductService productService;
    private ProductRespondDto response;

    @BeforeEach
    void setUp() {
        response = new ProductRespondDto();
        response.setId(1L);
        response.setName("Milk");
    }

    // -------------------------
    // GET /product
    // -------------------------
    @Test
    void testGetAllProducts() throws Exception{
        when(productService.getAll()).thenReturn(List.of(response));

        mockMvc.perform(get("/product")).andExpect(status().isOk())
        .andExpect(jsonPath("$[0].name").value("Milk"));
    }

    @Test
    void testGetAllProducts_returnNothing() throws Exception{
        when(productService.getAll()).thenReturn(List.of());

        mockMvc.perform(get("/product")).andExpect(status().isOk())
        .andExpect(jsonPath("$.length()").value(0));
    }

    // -------------------------
    // GET /product/add
    // -------------------------
    
}
