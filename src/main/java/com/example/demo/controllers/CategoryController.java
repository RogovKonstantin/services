package com.example.demo.controllers;

import com.example.demo.services.dtos.CategoryDTO;
import com.example.demo.services.CategoryService;
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
@RequestMapping("/categories")
public class CategoryController {

    private CategoryService categoryService;

    @PostMapping("/create")
    public EntityModel<CategoryDTO> createCategory(@RequestBody CategoryDTO categoryDTO) {
        CategoryDTO createdCategory = categoryService.createCategory(categoryDTO);
        return categoryService.createCategoryModel(createdCategory, true);
    }

    @PutMapping("/update/{id}")
    public EntityModel<CategoryDTO> updateCategory(@PathVariable UUID id, @RequestBody CategoryDTO categoryDTO) {
        CategoryDTO updatedCategory = categoryService.updateCategory(id, categoryDTO);
        return categoryService.createCategoryModel(updatedCategory, false);
    }

    @GetMapping("/{id}")
    public EntityModel<CategoryDTO> getCategoryById(@PathVariable UUID id) {
        CategoryDTO category = categoryService.getCategoryById(id);
        return categoryService.createCategoryModel(category, false);
    }

    @GetMapping
    public CollectionModel<EntityModel<CategoryDTO>> getAllCategories() {
        List<EntityModel<CategoryDTO>> categories = categoryService.getAllCategories().stream()
                .map(category -> categoryService.createCategoryModel(category, false))
                .collect(Collectors.toList());

        return CollectionModel.of(categories,
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(CategoryController.class).getAllCategories()).withSelfRel());
    }


    @Autowired
    public void setCategoryService(CategoryService categoryService) {
        this.categoryService = categoryService;
    }
}
