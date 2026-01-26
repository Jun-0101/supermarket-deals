package com.example.supermarket_deals.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.example.supermarket_deals.entity.Offer;
import com.example.supermarket_deals.service.OfferService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/offers")
@RequiredArgsConstructor
public class OfferController {
    private final OfferService offerService;

    @GetMapping("/supermarket")
    public List<Offer> getActiveOffersBySupermarket(
        @RequestParam String name,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
            if (date == null) date = LocalDate.now();

            return offerService.getActiveOffersBySupermarketName(name, date);
        }

    @GetMapping("/product")
    public List<Offer> getActiveOffersByProductName(
        @RequestParam String name,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
            if (date == null) date = LocalDate.now();

            return offerService.getActiveOffersByProductName(name, date);
        }
};
