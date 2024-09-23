package com.example.demo.controllers.exceptions.entityNotFoundExceptions;

import java.util.UUID;

public class UserNotFoundException extends EntityNotFoundException {
    public UserNotFoundException(UUID id) {
        super("User", id);
    }
}
