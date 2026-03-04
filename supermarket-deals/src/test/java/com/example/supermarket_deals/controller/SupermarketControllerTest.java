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
    // GET /supermarket
    // -------------------------
    @Test
    void testReturnAllSupermarkets() throws Exception {
        Supermarket supermarket1 = Supermarket.builder().name("rewe").build();
        Supermarket supermarket2 = Supermarket.builder().name("aldi").build();
        when(supermarketService.getAll()).thenReturn(List.of(supermarket1, supermarket2));

        mockMvc.perform(get("/supermarket"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].name").value("rewe"))
            .andExpect(jsonPath("$[1].name").value("aldi"));

        verify(supermarketService).getAll();
    }

    // -------------------------
    // POST /supermarket/add
    // -------------------------
    @Test
    void testAddSupermarket() throws Exception {
        Supermarket input = new Supermarket();
        input.setName("rewe");
        Supermarket saved = new Supermarket(1L, "rewe");
        when(supermarketService.save(any())).thenReturn(saved);

        mockMvc.perform(post("/supermarket/add").contentType(MediaType.APPLICATION_JSON_VALUE).content(Objects.requireNonNull(objectMapper.writeValueAsString(input))))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.name").value("rewe"));

        verify(supermarketService).save(any());        
    }

    // -------------------------
    // POST /supermarket/addMany
    // -------------------------
    @Test
    void testAddManySupermarkets() throws Exception {
        List<Supermarket> input = List.of(new Supermarket(null, "rewe"), new Supermarket(null, "aldi"));
        List<Supermarket> saved = List.of(new Supermarket(1L, "rewe"), new Supermarket(2L, "aldi"));
        when(supermarketService.saveMany(any())).thenReturn(saved);

        mockMvc.perform(post("/supermarket/addMany").contentType(MediaType.APPLICATION_JSON_VALUE).content(Objects.requireNonNull(objectMapper.writeValueAsString(input))))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$[0].id").value(1))
            .andExpect(jsonPath("$[0].name").value("rewe"))
            .andExpect(jsonPath("$[1].id").value(2))
            .andExpect(jsonPath("$[1].name").value("aldi"));

        verify(supermarketService).saveMany(any());        
    }

    // --------------------------
    // DELETE /supermarket/delete/{id} 
    // --------------------------
    @Test
    void testdDeleteSupermarket() throws Exception {
        mockMvc.perform(delete("/supermarket/delete/2"))
            .andExpect(status().isOk());
        verify(supermarketService).delete(2L);
    }

}
