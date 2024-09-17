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
import org.modelmapper.ModelMapper;
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

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public ListingDTO createListing(ListingDTO listingDTO) {
        Listing listing = modelMapper.map(listingDTO, Listing.class);

        Category category = categoryRepository.findById(listingDTO.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));
        listing.setCategory(category);

        User user = userRepository.findById(listingDTO.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        listing.setUser(user);

        Listing savedListing = listingRepository.save(listing);
        return modelMapper.map(savedListing, ListingDTO.class);
    }

    @Override
    public ListingDTO updateListing(UUID id, ListingDTO listingDTO) {
        Listing listing = listingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Listing not found"));

        modelMapper.map(listingDTO, listing); // Update existing listing with the new values
        listing.setStatus(ListingStatus.valueOf(listingDTO.getStatus())); // separately handle status if needed

        Listing updatedListing = listingRepository.save(listing);
        return modelMapper.map(updatedListing, ListingDTO.class);
    }

    @Override
    public void deleteListing(UUID id) {
        listingRepository.deleteById(id);
    }

    @Override
    public ListingDTO getListingById(UUID id) {
        Listing listing = listingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Listing not found"));
        return modelMapper.map(listing, ListingDTO.class);
    }

    @Override
    public List<ListingDTO> getAllListings() {
        List<Listing> listings = listingRepository.findAll();
        return listings.stream()
                .map(listing -> modelMapper.map(listing, ListingDTO.class))
                .collect(Collectors.toList());
    }
}
