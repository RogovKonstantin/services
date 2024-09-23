package com.example.demo.controllers.exceptions.entityNotFoundExceptions;

import java.util.UUID;

public class CategoryNotFoundException extends EntityNotFoundException {
    public CategoryNotFoundException(UUID id) {
        super("Listing", id);
    }
}
