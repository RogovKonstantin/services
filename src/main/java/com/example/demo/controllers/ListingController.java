package com.example.demo.controllers;

import com.example.demo.services.dtos.ListingDTO;
import com.example.demo.services.ListingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/listings")
public class ListingController {

    private ListingService listingService;

    @Autowired
    public ListingController(ListingService listingService) {
        this.listingService = listingService;
    }

    @GetMapping("/{id}")
    public EntityModel<ListingDTO> getListingById(@PathVariable UUID id) {
        ListingDTO listingDTO = listingService.getListingById(id);
        return listingService.createListingModel(listingDTO);
    }

    @PostMapping("/create")
    public EntityModel<ListingDTO> createListing(@RequestBody ListingDTO listingDTO) {
        ListingDTO createdListing = listingService.createListing(listingDTO);
        return listingService.createListingModel(createdListing);
    }

    @GetMapping
    public CollectionModel<EntityModel<ListingDTO>> getAllListings() {
        List<EntityModel<ListingDTO>> listings = listingService.getAllListings().stream()
                .map(listingService::createListingModel)
                .collect(Collectors.toList());

        return CollectionModel.of(listings,
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ListingController.class).getAllListings()).withSelfRel());
    }

    @PutMapping("update/{id}")
    public EntityModel<ListingDTO> updateListing(@PathVariable UUID id, @RequestBody ListingDTO listingDTO) {
        ListingDTO updatedListing = listingService.updateListing(id, listingDTO);
        return listingService.createListingModel(updatedListing);
    }
}
