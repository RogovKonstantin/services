package com.example.demo.controllers.exceptions.entityNotFoundExceptions;

import java.util.UUID;

public class ListingNotFoundException extends EntityNotFoundException {
    public ListingNotFoundException(UUID id) {
        super("Listing", id);
    }
}
