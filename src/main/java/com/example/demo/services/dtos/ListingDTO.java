package com.example.demo.services.dtos;
import java.util.UUID;
import java.util.List;

public class ListingDTO {
    private UUID id;
    private String title;
    private String description;
    private Double price;
    private String location;
    private String status;
    private UUID categoryId;
    private UUID userId;
    private List<UUID> auditLogIds;


    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public UUID getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(UUID categoryId) {
        this.categoryId = categoryId;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public List<UUID> getAuditLogIds() {
        return auditLogIds;
    }

    public void setAuditLogIds(List<UUID> auditLogIds) {
        this.auditLogIds = auditLogIds;
    }
}