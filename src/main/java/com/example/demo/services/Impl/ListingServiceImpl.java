package com.example.demo.services.Impl;

import com.example.demo.models.ListingStatus;
import com.example.demo.services.dtos.ListingDTO;
import com.example.demo.models.Category;
import com.example.demo.models.Listing;
import com.example.demo.models.User;
import com.example.demo.repositories.CategoryRepository;
import com.example.demo.repositories.ListingRepository;
import com.example.demo.repositories.UserRepository;
import com.example.demo.services.ListingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ListingServiceImpl implements ListingService {

    @Autowired
    private ListingRepository listingRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public ListingDTO createListing(ListingDTO listingDTO) {
        Listing listing = new Listing();
        listing.setTitle(listingDTO.getTitle());
        listing.setDescription(listingDTO.getDescription());
        listing.setPrice(listingDTO.getPrice());
        listing.setLocation(listingDTO.getLocation());
        listing.setStatus(ListingStatus.valueOf(listingDTO.getStatus()));

        Category category = categoryRepository.findById(listingDTO.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));
        listing.setCategory(category);

        User user = userRepository.findById(listingDTO.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        listing.setUser(user);

        Listing savedListing = listingRepository.save(listing);
        return convertToDTO(savedListing);
    }

    @Override
    public ListingDTO updateListing(UUID id, ListingDTO listingDTO) {
        Listing listing = listingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Listing not found"));

        listing.setTitle(listingDTO.getTitle());
        listing.setDescription(listingDTO.getDescription());
        listing.setPrice(listingDTO.getPrice());
        listing.setLocation(listingDTO.getLocation());
        listing.setStatus(ListingStatus.valueOf(listingDTO.getStatus()));

        Listing updatedListing = listingRepository.save(listing);
        return convertToDTO(updatedListing);
    }

    @Override
    public void deleteListing(UUID id) {
        listingRepository.deleteById(id);
    }

    @Override
    public ListingDTO getListingById(UUID id) {
        Listing listing = listingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Listing not found"));
        return convertToDTO(listing);
    }

    @Override
    public List<ListingDTO> getAllListings() {
        List<Listing> listings = listingRepository.findAll();
        return listings.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    private ListingDTO convertToDTO(Listing listing) {
        ListingDTO dto = new ListingDTO();
        dto.setId(listing.getId());
        dto.setTitle(listing.getTitle());
        dto.setDescription(listing.getDescription());
        dto.setPrice(listing.getPrice());
        dto.setLocation(listing.getLocation());
        dto.setStatus(listing.getStatus().name());
        dto.setCategoryId(listing.getCategory().getId());
        dto.setUserId(listing.getUser().getId());
        return dto;
    }
}
