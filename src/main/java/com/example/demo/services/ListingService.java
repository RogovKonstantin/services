package com.example.demo.services;


import com.example.demo.services.dtos.ListingDTO;

import java.util.List;
import java.util.UUID;

public interface ListingService {
    ListingDTO createListing(ListingDTO listingDTO);
    ListingDTO updateListing(UUID id, ListingDTO listingDTO);
    void deleteListing(UUID id);
    ListingDTO getListingById(UUID id);
    List<ListingDTO> getAllListings();
}
