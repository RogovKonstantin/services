package com.example.demo.models;

import com.example.demo.models.basemodels.IdDateTimeModel;
import jakarta.persistence.*;

@Entity
@Table(name = "audit_logs")
public class AuditLog extends IdDateTimeModel {

    private String action;

    private String details;

    private Listing listing;

    @Column(nullable = false)
    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    @Column(nullable = false)
    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    @ManyToOne
    @JoinColumn(name = "listing_id", nullable = false)
    public Listing getListing() {
        return listing;
    }

    public void setListing(Listing listing) {
        this.listing = listing;
    }
}