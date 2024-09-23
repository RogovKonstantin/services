package com.example.demo.services;

import com.example.demo.services.dtos.CategoryDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface CategoryService {
    CategoryDTO createCategory(CategoryDTO categoryDTO);

    CategoryDTO updateCategory(UUID id, CategoryDTO categoryDTO);

    void deleteCategory(UUID id);

    CategoryDTO getCategoryById(UUID id);

    Page<CategoryDTO> getAllCategories(Pageable pageable);
}
