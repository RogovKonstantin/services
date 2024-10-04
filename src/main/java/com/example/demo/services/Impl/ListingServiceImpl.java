package com.example.demo.services.Impl;

import com.example.demo.models.*;
import com.example.demo.repositories.*;
import com.example.demo.services.ListingService;
import com.example.demo.services.dtos.ListingDTO;
import com.example.demo.controllers.exceptions.entityNotFoundExceptions.ListingNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;

@Service
public class ListingServiceImpl implements ListingService {

    private final ListingRepository listingRepository;
    private final ModelMapper modelMapper;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final AuditLogRepository auditLogRepository;

    @Autowired
    public ListingServiceImpl(ListingRepository listingRepository, ModelMapper modelMapper,
                              CategoryRepository categoryRepository, UserRepository userRepository,
                              AuditLogRepository auditLogRepository) {
        this.listingRepository = listingRepository;
        this.modelMapper = modelMapper;
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
        this.auditLogRepository = auditLogRepository;
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
        Listing savedListing = listingRepository.saveAndFlush(listing);

        logAction("CREATE", savedListing, "Created a new listing");

        return modelMapper.map(savedListing, ListingDTO.class);
    }

    @Override
    public ListingDTO patchListing(UUID id, ListingDTO listingDTO) {
        Listing listing = listingRepository.findById(id)
                .orElseThrow(() -> new ListingNotFoundException(id));

        if (listingDTO.getTitle() != null) {
            listing.setTitle(listingDTO.getTitle());
        }
        if (listingDTO.getDescription() != null) {
            listing.setDescription(listingDTO.getDescription());
        }
        if (listingDTO.getPrice() != null) {
            listing.setPrice(listingDTO.getPrice());
        }
        if (listingDTO.getLocation() != null) {
            listing.setLocation(listingDTO.getLocation());
        }

        Listing patchedListing = listingRepository.saveAndFlush(listing);

        logAction("UPDATE", patchedListing, "Updated listing details");

        return modelMapper.map(patchedListing, ListingDTO.class);
    }

    @Override
    public void softDeleteListing(UUID id) {
        Listing listing = listingRepository.findById(id)
                .orElseThrow(() -> new ListingNotFoundException(id));
        listing.setDeleted(true);
        listingRepository.saveAndFlush(listing);

        logAction("DELETE", listing, "Soft deleted listing");
    }

    @Override
    public Page<ListingDTO> getAllListings(Pageable pageable) {
        Page<Listing> listingPage = listingRepository.findAllByDeletedFalseAndStatus(pageable, ListingStatus.ACCEPTED);
        return listingPage.map(listing -> modelMapper.map(listing, ListingDTO.class));
    }

    @Override
    public ListingDTO getListingById(UUID id) {
        Listing listing = listingRepository.findByIdAndDeletedFalseAndStatus(id, ListingStatus.ACCEPTED)
                .orElseThrow(() -> new ListingNotFoundException(id));
        return modelMapper.map(listing, ListingDTO.class);
    }


    private void logAction(String action, Listing listing, String details) {
        AuditLog auditLog = new AuditLog();
        auditLog.setAction(action);
        auditLog.setListing(listing);
        auditLog.setDetails(details);
        auditLogRepository.save(auditLog);
    }
}
