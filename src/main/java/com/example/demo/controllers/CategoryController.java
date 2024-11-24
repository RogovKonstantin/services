package com.example.demo.controllers;

import com.example.contract.controllers.CategoryApi;
import com.example.contract.dtos.*;
import com.example.demo.services.CategoryService;
import com.example.demo.services.dtos.CategoryDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
public class CategoryController implements CategoryApi {

    private final CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @Override
    public ResponseEntity<EntityModel<CategoryResponse>> getCategoryById(UUID id) {
        CategoryDTO categoryDTO = categoryService.getCategoryById(id);
        CategoryResponse response = toResponse(categoryDTO);
        return ResponseEntity.ok(toEntityModel(response));
    }

    @Override
    public ResponseEntity<EntityModel<CategoryResponse>> createCategory(CategoryRequest categoryRequest) {
        CategoryDTO categoryDTO = toDTO(categoryRequest);
        CategoryDTO createdCategory = categoryService.createCategory(categoryDTO);
        CategoryResponse response = toResponse(createdCategory);
        return ResponseEntity.created(
                linkTo(methodOn(CategoryController.class).getCategoryById(response.id())).toUri()
        ).body(toEntityModel(response));
    }

    @Override
    public ResponseEntity<PagedModel<EntityModel<CategoryResponse>>> getAllCategories(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<CategoryDTO> categoryDTOS = categoryService.getAllCategories(pageable);

        Page<CategoryResponse> responsePage = categoryDTOS.map(this::toResponse);

        PagedModel.PageMetadata metadata = new PagedModel.PageMetadata(
                responsePage.getSize(),
                responsePage.getNumber(),
                responsePage.getTotalElements()
        );

        List<EntityModel<CategoryResponse>> content = responsePage.getContent().stream()
                .map(this::toEntityModel)
                .toList();

        PagedModel<EntityModel<CategoryResponse>> pagedModel = PagedModel.of(content, metadata);

        if (responsePage.hasNext()) {
            pagedModel.add(linkTo(methodOn(CategoryController.class)
                    .getAllCategories(pageable.next().getPageNumber(), pageable.getPageSize()))
                    .withRel("next"));
        }
        if (responsePage.hasPrevious()) {
            pagedModel.add(linkTo(methodOn(CategoryController.class)
                    .getAllCategories(pageable.previousOrFirst().getPageNumber(), pageable.getPageSize()))
                    .withRel("previous"));
        }
        pagedModel.add(linkTo(methodOn(CategoryController.class)
                .getAllCategories(pageable.getPageNumber(), pageable.getPageSize()))
                .withSelfRel());

        return ResponseEntity.ok(pagedModel);
    }

    @Override
    public ResponseEntity<EntityModel<CategoryResponse>> updateCategory(UUID id, CategoryRequest categoryRequest) {
        CategoryDTO categoryDTO = toDTO(categoryRequest);
        CategoryDTO patchedCategory = categoryService.updateCategory(id, categoryDTO);
        CategoryResponse response = toResponse(patchedCategory);
        return ResponseEntity.ok(toEntityModel(response));
    }

    @Override
    public ResponseEntity<Void> deleteCategory(UUID id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }


    private CategoryDTO toDTO(CategoryRequest request) {
        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setName(request.name());
        return categoryDTO;
    }

    private CategoryResponse toResponse(CategoryDTO dto) {
        return new CategoryResponse(dto.getId(), dto.getName());
    }

    private EntityModel<CategoryResponse> toEntityModel(CategoryResponse response) {
        return EntityModel.of(response,
                linkTo(methodOn(CategoryController.class).getCategoryById(response.id())).withSelfRel(),
                linkTo(methodOn(CategoryController.class).getAllCategories(0, 3)).withRel("all"),
                linkTo(methodOn(CategoryController.class).updateCategory(response.id(), null)).withRel("update"),
                linkTo(methodOn(CategoryController.class).deleteCategory(response.id())).withRel("delete"));
    }
}

