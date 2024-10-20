package com.example.demo.controllers;

import com.example.demo.controllers.assemblers.ListingModelAssembler;
import com.example.demo.services.dtos.ListingDTO;
import com.example.demo.services.ListingService;
import com.example.demo.services.RabbitMQPublisher;
import com.example.demo.config.RabbitMQConfiguration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping("/listings")
public class ListingController {

    private final ListingService listingService;
    private final ListingModelAssembler assembler;
    private final RabbitMQPublisher rabbitMQPublisher;

    @Autowired
    public ListingController(ListingService listingService, ListingModelAssembler assembler, RabbitMQPublisher rabbitMQPublisher) {
        this.listingService = listingService;
        this.assembler = assembler;
        this.rabbitMQPublisher = rabbitMQPublisher;
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<ListingDTO>> getListingById(@PathVariable UUID id) {
        ListingDTO listingDTO = listingService.getListingById(id);
        return ResponseEntity.ok(assembler.toModel(listingDTO));
    }

    @PostMapping
    public ResponseEntity<EntityModel<ListingDTO>> createListing(@Valid @RequestBody ListingDTO listingDTO) {
        ListingDTO createdListing = listingService.createListing(listingDTO);

        rabbitMQPublisher.sendMessage(RabbitMQConfiguration.createQueueName, createdListing);

        return ResponseEntity.created(assembler.toModel(createdListing).getRequiredLink("self").toUri())
                .body(assembler.toModel(createdListing));
    }

    @GetMapping
    public ResponseEntity<PagedModel<EntityModel<ListingDTO>>> getAllListings(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "3") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<ListingDTO> listings = listingService.getAllListings(pageable);
        PagedModel<EntityModel<ListingDTO>> pagedModel = assembler.toPagedModel(listings, pageable);

        return ResponseEntity.ok(pagedModel);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<EntityModel<ListingDTO>> patchListing(
            @PathVariable UUID id,
            @Valid @RequestBody ListingDTO listingDTO) {
        ListingDTO patchedListing = listingService.patchListing(id, listingDTO);

        // Send to the update queue
        rabbitMQPublisher.sendMessage(RabbitMQConfiguration.updateQueueName, patchedListing);

        return ResponseEntity.ok(assembler.toModel(patchedListing));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> softDeleteListing(@PathVariable UUID id) {
        listingService.softDeleteListing(id);

        // Send to the delete queue
        rabbitMQPublisher.sendMessage(RabbitMQConfiguration.deleteQueueName, "Deleted listing with ID: " + id);

        return ResponseEntity.noContent().build();
    }
}
