package com.example.demo.models;

import com.example.demo.models.basemodels.IdDateTimeModel;
import jakarta.persistence.*;

@Entity
@Table(name = "audit_logs")
public class AuditLog extends IdDateTimeModel {

    @Column(nullable = false)
    private String action;

    @Column(nullable = false)
    private String details;

    @ManyToOne
    @JoinColumn(name = "listing_id", nullable = false)
    private Listing listing;


    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public Listing getListing() {
        return listing;
    }

    public void setListing(Listing listing) {
        this.listing = listing;
    }
}