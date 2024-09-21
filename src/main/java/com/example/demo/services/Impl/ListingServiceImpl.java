package com.example.demo.services.Impl;
import com.example.demo.models.ListingStatus;
import com.example.demo.models.User;
import com.example.demo.models.Category;
import com.example.demo.repositories.CategoryRepository;
import com.example.demo.repositories.UserRepository;
import com.example.demo.services.dtos.ListingDTO;
import com.example.demo.services.ListingService;
import com.example.demo.models.Listing;
import com.example.demo.repositories.ListingRepository;
import com.example.demo.controllers.exceptions.ListingNotFoundException;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ListingServiceImpl implements ListingService {

    private final ListingRepository listingRepository;
    private final ModelMapper modelMapper;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    @Autowired
    public ListingServiceImpl(ListingRepository listingRepository, ModelMapper modelMapper, CategoryRepository categoryRepository, UserRepository userRepository) {
        this.listingRepository = listingRepository;
        this.modelMapper = modelMapper;
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
    }


    @Override
    public ListingDTO createListing(ListingDTO listingDTO) {
        Listing listing = new Listing();
        listing.setTitle(listingDTO.getTitle());
        listing.setDescription(listingDTO.getDescription());
        listing.setPrice(listingDTO.getPrice());
        listing.setLocation(listingDTO.getLocation());

        Category category = categoryRepository.findById(listingDTO.getCategoryId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid Category ID: " + listingDTO.getCategoryId()));
        User user = userRepository.findById(listingDTO.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid User ID: " + listingDTO.getUserId()));


        listing.setCategory(category);
        listing.setUser(user);
        listing.setStatus(ListingStatus.PENDING);
        Listing savedListing = listingRepository.save(listing);
        return modelMapper.map(savedListing, ListingDTO.class);
    }


    @Override
    public ListingDTO updateListing(UUID id, ListingDTO listingDTO) {
        Listing listing = listingRepository.findById(id)
                .orElseThrow(() -> new ListingNotFoundException(id));
        modelMapper.map(listingDTO, listing);
        Listing updatedListing = listingRepository.save(listing);
        return modelMapper.map(updatedListing, ListingDTO.class);
    }

    @Override
    public void deleteListing(UUID id) {
        Listing listing = listingRepository.findById(id)
                .orElseThrow(() -> new ListingNotFoundException(id));
        listingRepository.delete(listing);
    }

    @Override
    public ListingDTO getListingById(UUID id) {
        Listing listing = listingRepository.findById(id)
                .orElseThrow(() -> new ListingNotFoundException(id));
        return modelMapper.map(listing, ListingDTO.class);
    }

    @Override
    public Page<ListingDTO> getAllListings(Pageable pageable) {
        Page<Listing> listingPage = listingRepository.findAll(pageable);
        return listingPage.map(listing -> modelMapper.map(listing, ListingDTO.class));
    }
}
