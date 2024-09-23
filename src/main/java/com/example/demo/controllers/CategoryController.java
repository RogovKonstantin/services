package com.example.demo.controllers;

import com.example.demo.controllers.assemblers.CategoryModelAssembler;
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

import javax.validation.Valid;
import java.lang.reflect.InvocationTargetException;
import java.util.UUID;

@RestController
@RequestMapping("/categories")
public class CategoryController {

    private final CategoryService categoryService;
    private final CategoryModelAssembler assembler;

    @Autowired
    public CategoryController(CategoryService categoryService, CategoryModelAssembler assembler) {
        this.categoryService = categoryService;
        this.assembler = assembler;
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<CategoryDTO>> getCategoryById(@PathVariable UUID id) {
        CategoryDTO categoryDTO = categoryService.getCategoryById(id);
        return ResponseEntity.ok(assembler.toModel(categoryDTO));
    }

    @PostMapping
    public ResponseEntity<EntityModel<CategoryDTO>> createCategory(@Valid @RequestBody CategoryDTO categoryDTO) {
        CategoryDTO createdCategory = categoryService.createCategory(categoryDTO);
        return ResponseEntity.created(assembler.toModel(createdCategory).getRequiredLink("self").toUri())
                .body(assembler.toModel(createdCategory));
    }

    @GetMapping
    public ResponseEntity<PagedModel<EntityModel<CategoryDTO>>> getAllCategories (
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "3") int size)  {

        Pageable pageable = PageRequest.of(page, size);
        Page<CategoryDTO> categories = categoryService.getAllCategories(pageable);
        PagedModel<EntityModel<CategoryDTO>> pagedModel = assembler.toPagedModel(categories, pageable);

        return ResponseEntity.ok(pagedModel);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<CategoryDTO>> updateCategory(@PathVariable UUID id,
                                                                   @Valid @RequestBody CategoryDTO categoryDTO) {
        CategoryDTO updatedCategory = categoryService.updateCategory(id, categoryDTO);
        return ResponseEntity.ok(assembler.toModel(updatedCategory));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable UUID id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }
}
