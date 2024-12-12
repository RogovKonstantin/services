package com.example.demo.services.Impl;

import com.example.contract.exceptions.CategoryNotFoundException;
import com.example.contract.exceptions.ListingNotFoundException;
import com.example.contract.exceptions.ListingValidationException;
import com.example.contract.exceptions.UserNotFoundException;
import com.example.demo.models.*;
import com.example.demo.repositories.*;
import com.example.demo.services.ListingService;
import com.example.demo.services.RabbitMQPublisher;
import com.example.demo.services.dtos.ListingDTO;
import com.example.demo.services.dtos.ValidationRequestMessage;
import com.example.demo.services.dtos.ValidationResponseMessage;
import com.example.validation.ListingValidationProto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.modelmapper.ModelMapper;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ListingServiceImpl implements ListingService {

    private final ListingRepository listingRepository;
    private final ModelMapper modelMapper;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final RabbitMQPublisher rabbitMQPublisher;
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    public ListingServiceImpl(ListingRepository listingRepository, ModelMapper modelMapper,
                              CategoryRepository categoryRepository, UserRepository userRepository, RabbitMQPublisher rabbitMQPublisher) {
        this.listingRepository = listingRepository;
        this.modelMapper = modelMapper;
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
        this.rabbitMQPublisher = rabbitMQPublisher;
    }


    @Override
    public ListingDTO createListing(ListingDTO listingDTO) {
        Listing listing = new Listing();
        listing.setTitle(listingDTO.getTitle());
        listing.setDescription(listingDTO.getDescription());
        listing.setPrice(listingDTO.getPrice());
        listing.setLocation(listingDTO.getLocation());

        Category category = categoryRepository.findById(listingDTO.getCategoryId())
                .orElseThrow(() -> new CategoryNotFoundException(listingDTO.getCategoryId()));
        User user = userRepository.findById(listingDTO.getUserId())
                .orElseThrow(() -> new UserNotFoundException(listingDTO.getUserId()));

        listing.setCategory(category);
        listing.setUser(user);

        // Listing is created as PENDING until validation result is known
        listing.setStatus(ListingStatus.PENDING);
        Listing savedListing = listingRepository.save(listing);

        // Create a validation request message
        ValidationRequestMessage requestMessage = new ValidationRequestMessage(
                savedListing.getId(),
                savedListing.getTitle(),
                savedListing.getDescription()
        );

        // Send to validationQueue to be processed asynchronously
        rabbitMQPublisher.sendMessage("listings.validate", requestMessage);

        // Return PENDING listing. Client can check back later or be notified when it's accepted/rejected.
        return modelMapper.map(savedListing, ListingDTO.class);
    }

    // Listen for validation responses on a separate queue
    @RabbitListener(queues = "validationResponseQueue")
    public void handleValidationResponse(ValidationResponseMessage responseMessage) {
        // Now responseMessage is already deserialized
        Listing listing = listingRepository.findById(responseMessage.getListingId())
                .orElseThrow(() -> new ListingNotFoundException(responseMessage.getListingId()));

        if (responseMessage.isValid()) {
            listing.setStatus(ListingStatus.ACCEPTED);
        } else {
            listing.setStatus(ListingStatus.REJECTED);
        }

        listingRepository.save(listing);
    }


    @Override
    public ListingDTO patchListing(UUID id, ListingDTO listingDTO) {
        Listing listing = listingRepository.findByIdAndDeletedFalse(id)
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


        return modelMapper.map(patchedListing, ListingDTO.class);
    }

    @Override
    public void softDeleteListing(UUID id) {
        Listing listing = listingRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new ListingNotFoundException(id));
        listing.setDeleted(true);
        listingRepository.saveAndFlush(listing);

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


}
