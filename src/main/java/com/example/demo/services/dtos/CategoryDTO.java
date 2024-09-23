package com.example.demo.services.dtos;

import javax.validation.constraints.NotBlank;
import java.util.UUID;

public class CategoryDTO {
    private UUID id;

    @NotBlank(message = "Name is required")
    private String name;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
