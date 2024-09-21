package com.example.demo.controllers.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.UUID;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ListingNotFoundException extends RuntimeException {
    public ListingNotFoundException(UUID id) {
        super("Listing not found with ID: " + id);
    }
}
