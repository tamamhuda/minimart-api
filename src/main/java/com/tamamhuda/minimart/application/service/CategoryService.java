package com.tamamhuda.minimart.application.service;

import com.tamamhuda.minimart.application.dto.CategoryDto;
import com.tamamhuda.minimart.application.dto.CategoryRequestDto;
import com.tamamhuda.minimart.domain.entity.Category;

import java.util.List;
import java.util.UUID;

public interface CategoryService {
    Category getById(UUID categoryId);

    Category getByName(String name);

    Category getByIdOrName(String IdOrName);

    CategoryDto create(CategoryRequestDto request);

    CategoryDto update(CategoryRequestDto request, UUID categoryId);

    void delete(UUID categoryId);

    List<CategoryDto> getAllCategories();
}
