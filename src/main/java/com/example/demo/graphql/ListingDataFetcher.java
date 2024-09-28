package com.example.demo.graphql;

import com.example.demo.services.ListingService;
import com.example.demo.services.dtos.ListingDTO;
import com.example.demo.controllers.exceptions.entityNotFoundExceptions.ListingNotFoundException;
import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsMutation;
import com.netflix.graphql.dgs.DgsQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.UUID;

@DgsComponent
public class ListingDataFetcher {

    private final ListingService listingService;

    public ListingDataFetcher(ListingService listingService) {
        this.listingService = listingService;
    }

    @DgsQuery
    public ListingDTO getListingById(String id) {
        try {
            return listingService.getListingById(UUID.fromString(id));
        } catch (IllegalArgumentException e) {
            throw new ListingNotFoundException(UUID.fromString(id));
        }
    }

    @DgsQuery
    public Page<ListingDTO> getAllListings(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return listingService.getAllListings(pageable);
    }

    @DgsMutation
    public ListingDTO createListing(String title, String description, Float price, String location, String categoryId, String userId) {
        ListingDTO listingDTO = new ListingDTO();
        listingDTO.setTitle(title);
        listingDTO.setDescription(description);
        listingDTO.setPrice(BigDecimal.valueOf(price));
        listingDTO.setLocation(location);
        listingDTO.setCategoryId(UUID.fromString(categoryId));
        listingDTO.setUserId(UUID.fromString(userId));
        return listingService.createListing(listingDTO);
    }

    @DgsMutation
    public ListingDTO updateListing(String id, String title, String description, Float price, String location) {
        ListingDTO listingDTO = new ListingDTO();
        listingDTO.setTitle(title);
        listingDTO.setDescription(description);
        listingDTO.setPrice(BigDecimal.valueOf(price));
        listingDTO.setLocation(location);
        return listingService.updateListing(UUID.fromString(id), listingDTO);
    }

    @DgsMutation
    public ListingDTO patchListing(String id, String title, String description, Float price, String location) {
        ListingDTO listingDTO = new ListingDTO();
        if (title != null) {
            listingDTO.setTitle(title);
        }
        if (description != null) {
            listingDTO.setDescription(description);
        }
        if (price != null) {
            listingDTO.setPrice(BigDecimal.valueOf(price));
        }
        if (location != null) {
            listingDTO.setLocation(location);
        }
        return listingService.patchListing(UUID.fromString(id), listingDTO);
    }

    @DgsMutation
    public Boolean deleteListing(String id) {
            listingService.deleteListing(UUID.fromString(id));
            return true;
    }
}
