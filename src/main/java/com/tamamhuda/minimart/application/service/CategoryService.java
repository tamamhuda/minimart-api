package com.tamamhuda.minimart.application.service;

import com.tamamhuda.minimart.application.dto.CategoryDto;
import com.tamamhuda.minimart.application.dto.CategoryRequestDto;
import com.tamamhuda.minimart.domain.entity.Category;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

public interface CategoryService {
    public Category getById(UUID categoryId);

    public Category getByName(String name);

    public Category getByIdOrName(String IdOrName);

    public ResponseEntity<CategoryDto> create(CategoryRequestDto request);

    public ResponseEntity<CategoryDto> update(CategoryRequestDto request, UUID categoryId);

    public ResponseEntity<?> delete(UUID categoryId);

    public ResponseEntity<List<CategoryDto>> getAllCategories();
}
