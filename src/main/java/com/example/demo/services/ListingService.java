package com.example.demo.services;


import com.example.demo.services.dtos.ListingDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.EntityModel;

import java.util.List;
import java.util.UUID;

public interface ListingService {
    ListingDTO createListing(ListingDTO listingDTO);
    ListingDTO patchListing(UUID id, ListingDTO listingDTO);
    void softDeleteListing(UUID id);
    ListingDTO getListingById(UUID id);
    Page<ListingDTO> getAllListings(Pageable pageable);
}
