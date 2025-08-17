package com.tamamhuda.minimart.application.service.impl;

import com.tamamhuda.minimart.application.dto.CategoryDto;
import com.tamamhuda.minimart.application.dto.CategoryRequestDto;
import com.tamamhuda.minimart.application.mapper.CategoryMapper;
import com.tamamhuda.minimart.application.mapper.CategoryRequestMapper;
import com.tamamhuda.minimart.application.service.CategoryService;
import com.tamamhuda.minimart.common.dto.PageDto;
import com.tamamhuda.minimart.domain.entity.Category;
import com.tamamhuda.minimart.domain.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
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
    public CategoryDto create(CategoryRequestDto request) {
        boolean isNameExist = categoryRepository.findByName(request.getName()).isPresent();

        if (isNameExist) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Category name already exists");
        }

        Category category = categoryRequestMapper.toEntity(request);
        Category savedCategory = categoryRepository.save(category);

        return categoryMapper.toDto(savedCategory);
    }

    @Override
    @CacheEvict(cacheNames = "categories", key = "'all-categories'")
    public CategoryDto update(CategoryRequestDto request, UUID categoryId) {
        Category category = getById(categoryId);

        categoryRequestMapper.updateFromRequestDto(request, category);
        category.setUpdatedAt(Instant.now());
        Category updatedCategory = categoryRepository.save(category);

        return categoryMapper.toDto(updatedCategory);
    }

    @Override
    @CacheEvict(cacheNames = "categories", key = "'all-categories'")
    public void delete(UUID categoryId) {
        Category category = getById(categoryId);
        categoryRepository.delete(category);
    }

    @Override
    @Cacheable(cacheNames = "categories", key = "'all-categories'")
    public PageDto<CategoryDto> getAllCategories(Pageable pageable) {
        Page<CategoryDto> page = categoryRepository.findAll(pageable).map(categoryMapper::toDto);
        return PageDto.<CategoryDto>builder()
                .content(page.getContent())
                .pageNumber(page.getNumber())
                .totalElements(page.getTotalElements())
                .pageSize(page.getSize())
                .totalPages(page.getTotalPages())
                .last(page.isLast())
                .build();
    }
}
