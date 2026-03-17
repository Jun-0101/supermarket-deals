package com.example.supermarket_deals.exception;

public class DealNotFoundException extends RuntimeException {
    public DealNotFoundException(Long id) {
        super("Deal with id " + id + " not found");
    }
}
