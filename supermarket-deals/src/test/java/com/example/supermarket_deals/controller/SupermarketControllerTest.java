package com.example.supermarket_deals.controller;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;

import java.util.List;
import java.util.Objects;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.example.supermarket_deals.entity.Supermarket;
import com.example.supermarket_deals.service.SupermarketService;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(SupermarketController.class)
public class SupermarketControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private SupermarketService supermarketService;
    @Autowired
    private ObjectMapper objectMapper;

    // -------------------------
    // GET /supermarketss
    // -------------------------
    @Test
    void testReturnAllSupermarkets() throws Exception {
        Supermarket supermarket1 = Supermarket.builder().name("rewe").build();
        Supermarket supermarket2 = Supermarket.builder().name("aldi").build();
        when(supermarketService.findAll()).thenReturn(List.of(supermarket1, supermarket2));

        mockMvc.perform(get("/supermarkets"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].name").value("rewe"))
            .andExpect(jsonPath("$[1].name").value("aldi"));

        verify(supermarketService).findAll();
    }

    // -------------------------
    // POST /supermarkets
    // -------------------------
    @Test
    void testAddSupermarket() throws Exception {
        Supermarket input = new Supermarket();
        input.setName("rewe");
        Supermarket saved = new Supermarket(1L, "rewe");
        when(supermarketService.save(any())).thenReturn(saved);

        mockMvc.perform(post("/supermarkets").contentType(MediaType.APPLICATION_JSON_VALUE).content(Objects.requireNonNull(objectMapper.writeValueAsString(input))))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.name").value("rewe"));

        verify(supermarketService).save(any());        
    }

    // --------------------------
    // DELETE /supermarkets/{id} 
    // --------------------------
    @Test
    void testdDeleteSupermarket() throws Exception {
        mockMvc.perform(delete("/supermarkets/2"))
            .andExpect(status().isOk());
        verify(supermarketService).delete(2L);
    }

}
