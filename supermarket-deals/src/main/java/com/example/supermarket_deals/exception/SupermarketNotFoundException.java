package com.example.supermarket_deals.exception;

public class SupermarketNotFoundException extends RuntimeException {
    public SupermarketNotFoundException(Long id) {
        super("Supermarket with id " + id + " not found");
    }
}
