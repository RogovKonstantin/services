package com.example.demo.controllers;

import com.example.contract.controllers.ListingApi;
import com.example.contract.dtos.ListingRequest;
import com.example.contract.dtos.ListingResponse;
import com.example.demo.services.dtos.ListingDTO;
import com.example.demo.services.ListingService;
import com.example.demo.services.RabbitMQPublisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@Validated
public class ListingController implements ListingApi {

    private final ListingService listingService;
    private final RabbitMQPublisher rabbitMQPublisher;

    @Autowired
    public ListingController(ListingService listingService, RabbitMQPublisher rabbitMQPublisher) {
        this.listingService = listingService;
        this.rabbitMQPublisher = rabbitMQPublisher;
    }

    @Override
    public ResponseEntity<PagedModel<EntityModel<ListingResponse>>> getAllListings(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ListingDTO> listings = listingService.getAllListings(pageable);

        Page<ListingResponse> responsePage = listings.map(this::toResponse);

        PagedModel.PageMetadata metadata = new PagedModel.PageMetadata(
                responsePage.getSize(),
                responsePage.getNumber(),
                responsePage.getTotalElements()
        );

        List<EntityModel<ListingResponse>> content = responsePage.getContent().stream()
                .map(this::toEntityModel)
                .toList();

        PagedModel<EntityModel<ListingResponse>> pagedModel = PagedModel.of(content, metadata);

        if (responsePage.hasNext()) {
            pagedModel.add(linkTo(methodOn(ListingController.class)
                    .getAllListings(pageable.next().getPageNumber(), pageable.getPageSize()))
                    .withRel("next"));
        }
        if (responsePage.hasPrevious()) {
            pagedModel.add(linkTo(methodOn(ListingController.class)
                    .getAllListings(pageable.previousOrFirst().getPageNumber(), pageable.getPageSize()))
                    .withRel("previous"));
        }
        pagedModel.add(linkTo(methodOn(ListingController.class)
                .getAllListings(pageable.getPageNumber(), pageable.getPageSize()))
                .withSelfRel());

        return ResponseEntity.ok(pagedModel);
    }

    @Override
    public ResponseEntity<EntityModel<ListingResponse>> getListingById(UUID id) {
        ListingDTO listingDTO = listingService.getListingById(id);
        ListingResponse response = toResponse(listingDTO);
        return ResponseEntity.ok(toEntityModel(response));
    }

    @Override
    public ResponseEntity<EntityModel<ListingResponse>> createListing(ListingRequest listingRequest) {
        ListingDTO listingDTO = toDTO(listingRequest);
        ListingDTO createdListing = listingService.createListing(listingDTO);
        ListingResponse response = toResponse(createdListing);

        rabbitMQPublisher.sendMessage("listings.create", response);

        return ResponseEntity.created(
                linkTo(methodOn(ListingController.class).getListingById(response.id())).toUri()
        ).body(toEntityModel(response));
    }

    @Override
    public ResponseEntity<EntityModel<ListingResponse>> patchListing(UUID id, ListingRequest listingRequest) {
        ListingDTO listingDTO = toDTO(listingRequest);
        ListingDTO patchedListing = listingService.patchListing(id, listingDTO);
        ListingResponse response = toResponse(patchedListing);

        rabbitMQPublisher.sendMessage("listings.update", response);

        return ResponseEntity.ok(toEntityModel(response));
    }

    @Override
    public ResponseEntity<Void> softDeleteListing(UUID id) {
        listingService.softDeleteListing(id);
        rabbitMQPublisher.sendMessage("listings.delete", "Deleted listing with ID: " + id);
        return ResponseEntity.noContent().build();
    }


    private ListingDTO toDTO(ListingRequest request) {
        ListingDTO dto = new ListingDTO();
        dto.setTitle(request.title());
        dto.setDescription(request.description());
        dto.setPrice(request.price());
        dto.setLocation(request.location());
        dto.setStatus(request.status());
        dto.setCategoryId(request.categoryId());
        dto.setUserId(request.userId());
        return dto;
    }


    private ListingResponse toResponse(ListingDTO dto) {
        return new ListingResponse(
                dto.getId(),
                dto.getTitle(),
                dto.getDescription(),
                dto.getPrice(),
                dto.getLocation(),
                dto.getStatus(),
                dto.getCategoryId(),
                dto.getUserId()
        );
    }


    private EntityModel<ListingResponse> toEntityModel(ListingResponse response) {
        return EntityModel.of(response,
                linkTo(methodOn(ListingController.class).getListingById(response.id())).withSelfRel(),
                linkTo(methodOn(ListingController.class).getAllListings(0, 10)).withRel("all"),
                linkTo(methodOn(ListingController.class).patchListing(response.id(), null)).withRel("update"),
                linkTo(methodOn(ListingController.class).softDeleteListing(response.id())).withRel("delete")
        );
    }
}

