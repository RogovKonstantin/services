package com.example.demo.models;

import com.example.demo.models.basemodels.IdDateTimeModel;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "users")
public class User extends IdDateTimeModel {

    private String username;

    private String email;

    private List<Listing> listings;

    @Column(nullable = false, unique = true)
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Column(nullable = false, unique = true)
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    public List<Listing> getListings() {
        return listings;
    }

    public void setListings(List<Listing> listings) {
        this.listings = listings;
    }
}