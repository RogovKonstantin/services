package com.example.demo.controllers;

import com.example.demo.services.dtos.ListingDTO;
import com.example.demo.services.ListingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/listings")
public class ListingController {


    private ListingService listingService;

    @GetMapping("/{id}")
    public EntityModel<ListingDTO> getListingById(@PathVariable UUID id) {
        ListingDTO listingDTO = listingService.getListingById(id);

        return EntityModel.of(listingDTO,
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ListingController.class).getListingById(id)).withSelfRel(),
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ListingController.class).getAllListings()).withRel("listings"),
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ListingController.class).updateListing(id, new ListingDTO())).withRel("update"));
    }

    @PostMapping("/create")
    public EntityModel<ListingDTO> createListing(@RequestBody ListingDTO listingDTO) {
        ListingDTO createdListing = listingService.createListing(listingDTO);

        return EntityModel.of(createdListing,
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ListingController.class).getListingById(createdListing.getId())).withSelfRel(),
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ListingController.class).getAllListings()).withRel("listings"),
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ListingController.class).updateListing(createdListing.getId(), new ListingDTO())).withRel("update"));
    }

    @GetMapping
    public CollectionModel<EntityModel<ListingDTO>> getAllListings() {
        List<EntityModel<ListingDTO>> listings = listingService.getAllListings().stream()
                .map(listingDTO -> EntityModel.of(listingDTO,
                        WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ListingController.class).getListingById(listingDTO.getId())).withSelfRel(),
                        WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ListingController.class).getAllListings()).withRel("listings")))
                .collect(Collectors.toList());

        return CollectionModel.of(listings,
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ListingController.class).getAllListings()).withSelfRel());
    }

    @PutMapping("update/{id}")
    public EntityModel<ListingDTO> updateListing(@PathVariable UUID id, @RequestBody ListingDTO listingDTO) {
        ListingDTO updatedListing = listingService.updateListing(id, listingDTO);

        return EntityModel.of(updatedListing,
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ListingController.class).getListingById(updatedListing.getId())).withSelfRel(),
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ListingController.class).getAllListings()).withRel("listings"));
    }

    @Autowired
    public void setListingService(ListingService listingService) {
        this.listingService = listingService;
    }
}
