package com.example.demo.controllers.assemblers;

import com.example.demo.controllers.ListingController;
import com.example.demo.services.dtos.ListingDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class ListingModelAssembler extends RepresentationModelAssemblerSupport<ListingDTO, EntityModel<ListingDTO>> {

    public ListingModelAssembler() {
        super(ListingController.class, (Class<EntityModel<ListingDTO>>) (Class<?>) EntityModel.class);
    }

    @Override
    public EntityModel<ListingDTO> toModel(ListingDTO listingDTO) {
        EntityModel<ListingDTO> model = EntityModel.of(listingDTO);

        model.add(linkTo(methodOn(ListingController.class).getListingById(listingDTO.getId())).withSelfRel());
        model.add(linkTo(methodOn(ListingController.class).updateListing(listingDTO.getId(), listingDTO)).withRel("update"));
        model.add(linkTo(methodOn(ListingController.class).deleteListing(listingDTO.getId())).withRel("delete"));

        return model;
    }

    public PagedModel<EntityModel<ListingDTO>> toPagedModel(Page<ListingDTO> listings, Pageable pageable) {
        PagedModel.PageMetadata metadata = new PagedModel.PageMetadata(
                listings.getSize(),
                listings.getNumber(),
                listings.getTotalElements()
        );

        PagedModel<EntityModel<ListingDTO>> pagedModel = PagedModel.of(listings.map(this::toModel).getContent(), metadata);
        if (listings.hasNext()) {
            pagedModel.add(linkTo(methodOn(ListingController.class)
                    .getAllListings(pageable.next().getPageNumber(), pageable.getPageSize()))
                    .withRel("next"));
        }
        if (listings.hasPrevious()) {
            pagedModel.add(linkTo(methodOn(ListingController.class)
                    .getAllListings(pageable.previousOrFirst().getPageNumber(), pageable.getPageSize()))
                    .withRel("previous"));
        }

        pagedModel.add(linkTo(methodOn(ListingController.class)
                .getAllListings(pageable.getPageNumber(), pageable.getPageSize()))
                .withSelfRel());

        return pagedModel;
    }
}
