/*
package com.example.demo.controllers.assemblers;

import com.example.demo.controllers.CategoryController;
import com.example.demo.services.dtos.CategoryDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class CategoryModelAssembler extends RepresentationModelAssemblerSupport<CategoryDTO, EntityModel<CategoryDTO>> {

    public CategoryModelAssembler() {
        super(CategoryController.class, (Class<EntityModel<CategoryDTO>>) (Class<?>) EntityModel.class);
    }

    @Override
    public EntityModel<CategoryDTO> toModel(CategoryDTO categoryDTO) {
        EntityModel<CategoryDTO> model = EntityModel.of(categoryDTO);

        model.add(linkTo(methodOn(CategoryController.class).getCategoryById(categoryDTO.getId())).withSelfRel());
        model.add(linkTo(methodOn(CategoryController.class).updateCategory(categoryDTO.getId(), categoryDTO)).withRel("update"));
        model.add(linkTo(methodOn(CategoryController.class).deleteCategory(categoryDTO.getId())).withRel("delete"));

        return model;
    }

    public PagedModel<EntityModel<CategoryDTO>> toPagedModel(Page<CategoryDTO> categories, Pageable pageable) {
        PagedModel.PageMetadata metadata = new PagedModel.PageMetadata(
                categories.getSize(),
                categories.getNumber(),
                categories.getTotalElements()
        );

        PagedModel<EntityModel<CategoryDTO>> pagedModel = PagedModel.of(categories.map(this::toModel).getContent(), metadata);
        if (categories.hasNext()) {
            pagedModel.add(linkTo(methodOn(CategoryController.class)
                    .getAllCategories(pageable.next().getPageNumber(), pageable.getPageSize()))
                    .withRel("next"));
        }
        if (categories.hasPrevious()) {
            pagedModel.add(linkTo(methodOn(CategoryController.class)
                    .getAllCategories(pageable.previousOrFirst().getPageNumber(), pageable.getPageSize()))
                    .withRel("previous"));
        }

        pagedModel.add(linkTo(methodOn(CategoryController.class)
                .getAllCategories(pageable.getPageNumber(), pageable.getPageSize()))
                .withSelfRel());

        return pagedModel;
    }
}
*/
