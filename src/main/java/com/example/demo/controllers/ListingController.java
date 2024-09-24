package com.example.demo.controllers;

import com.example.demo.controllers.assemblers.ListingModelAssembler;
import com.example.demo.services.dtos.ListingDTO;
import com.example.demo.services.ListingService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.lang.reflect.InvocationTargetException;
import java.util.UUID;

@RestController
@RequestMapping("/listings")
public class ListingController {

    private final ListingService listingService;
    private final ListingModelAssembler assembler;

    @Autowired
    public ListingController(ListingService listingService, ListingModelAssembler assembler) {
        this.listingService = listingService;
        this.assembler = assembler;
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<ListingDTO>> getListingById(@PathVariable UUID id) {
        ListingDTO listingDTO = listingService.getListingById(id);
        return ResponseEntity.ok(assembler.toModel(listingDTO));
    }

    @PostMapping
    public ResponseEntity<EntityModel<ListingDTO>> createListing(@Valid @RequestBody ListingDTO listingDTO) {
        ListingDTO createdListing = listingService.createListing(listingDTO);
        return ResponseEntity.created(assembler.toModel(createdListing).getRequiredLink("self").toUri())
                .body(assembler.toModel(createdListing));
    }

    @GetMapping
    public ResponseEntity<PagedModel<EntityModel<ListingDTO>>> getAllListings(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "3") int size){

        Pageable pageable = PageRequest.of(page, size);

        Page<ListingDTO> listings = listingService.getAllListings(pageable);


        PagedModel<EntityModel<ListingDTO>> pagedModel = assembler.toPagedModel(listings, pageable);

        return ResponseEntity.ok(pagedModel);
    }


    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<ListingDTO>> updateListing(@PathVariable UUID id,
                                                                 @Valid @RequestBody ListingDTO listingDTO) {
        ListingDTO updatedListing = listingService.updateListing(id, listingDTO);
        return ResponseEntity.ok(assembler.toModel(updatedListing));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<EntityModel<ListingDTO>> patchListing(
            @PathVariable UUID id,
            @Valid @RequestBody ListingDTO listingDTO) {
        ListingDTO updatedListing = listingService.patchListing(id, listingDTO);
        return ResponseEntity.ok(assembler.toModel(updatedListing));
    }


    @PatchMapping("/{id}/delete")
    public ResponseEntity<Void> softDeleteListing(@PathVariable UUID id) {
        listingService.deleteListing(id);
        return ResponseEntity.noContent().build();
    }

}
