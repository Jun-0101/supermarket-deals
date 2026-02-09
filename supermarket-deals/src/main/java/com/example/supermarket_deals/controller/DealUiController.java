package com.example.supermarket_deals.controller;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.example.supermarket_deals.service.DealService;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/deals-ui")
@RequiredArgsConstructor
public class DealUiController {

    private final DealService dealService;

    @GetMapping
    public String dealsPage() {
        return "deals";
    }

    @GetMapping("/product")
    public String searchByProduct(
            @RequestParam String name,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            Model model) {

        if (date == null) {
            date = LocalDate.now();
        }

        model.addAttribute(
                "productDeals",
                dealService.getActiveDealsByProductName(name, date)
        );

        return "deals";
    }

    @GetMapping("/supermarket")
    public String searchBySupermarket(
            @RequestParam String name,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            Model model) {

        if (date == null) {
            date = LocalDate.now();
        }

        model.addAttribute(
                "supermarketDeals",
                dealService.getActiveDealsBySupermarketName(name, date)
        );

        return "deals";
    }
}
