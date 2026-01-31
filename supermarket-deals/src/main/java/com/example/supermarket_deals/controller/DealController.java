package com.example.supermarket_deals.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.example.supermarket_deals.entity.Deal;
import com.example.supermarket_deals.service.DealService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/deals")
@RequiredArgsConstructor
public class DealController {
    private final DealService offerService;

    @GetMapping("/supermarket")
    public List<Deal> getActiveDealsBySupermarket(
        @RequestParam String name,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
            if (date == null) date = LocalDate.now();

            return offerService.getActiveDealsBySupermarketName(name, date);
        }

    @GetMapping("/product")
    public List<Deal> getActiveDealsByProductName(
        @RequestParam String name,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
            if (date == null) date = LocalDate.now();

            return offerService.getActiveDealsByProductName(name, date);
        }
};
