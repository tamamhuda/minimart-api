package com.tamamhuda.minimart.application.service.impl;

import com.tamamhuda.minimart.application.dto.CategoryDto;
import com.tamamhuda.minimart.application.dto.CategoryRequestDto;
import com.tamamhuda.minimart.application.mapper.CategoryMapper;
import com.tamamhuda.minimart.application.mapper.CategoryRequestMapper;
import com.tamamhuda.minimart.application.service.CategoryService;
import com.tamamhuda.minimart.domain.entity.Category;
import com.tamamhuda.minimart.domain.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;
    private final CategoryRequestMapper categoryRequestMapper;


    @Override
    public Category getById(UUID categoryId) {
        return categoryRepository.findById(categoryId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found")
        );
    }

    @Override
    public Category getByName(String name) {
        return categoryRepository.findByName(name).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found")
        );
    }

    @Override
    public Category getByIdOrName(String IdOrName) {
        try {
            UUID id = UUID.fromString(IdOrName);
            return getById(id);
        } catch (IllegalArgumentException e) {
            return getByName(IdOrName);
        }
    }

    @Override
    public ResponseEntity<CategoryDto> create(CategoryRequestDto request) {
        boolean isNameExist = categoryRepository.findByName(request.getName()).isPresent();

        if (isNameExist) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Category name already exists");
        }

        Category category = categoryRequestMapper.toEntity(request);
        Category savedCategory = categoryRepository.save(category);

        return ResponseEntity.status(HttpStatus.CREATED).body(categoryMapper.toDto(savedCategory));
    }

    @Override
    public ResponseEntity<CategoryDto> update(CategoryRequestDto request, UUID categoryId) {
        Category category = getById(categoryId);

        categoryRequestMapper.updateFromRequestDto(request, category);
        category.setUpdatedAt(Instant.now());
        Category updatedCategory = categoryRepository.save(category);

        return ResponseEntity.status(HttpStatus.OK).body(categoryMapper.toDto(updatedCategory));
    }

    @Override
    public ResponseEntity<?> delete(UUID categoryId) {
        Category category = getById(categoryId);
        categoryRepository.delete(category);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Override
    public ResponseEntity<List<CategoryDto>> getAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        return ResponseEntity.status(HttpStatus.OK).body(categoryMapper.toDto(categories));
    }
}
