package com.tamamhuda.minimart.application.service;

import com.tamamhuda.minimart.application.dto.CategoryDto;
import com.tamamhuda.minimart.application.dto.CategoryRequestDto;
import com.tamamhuda.minimart.common.dto.PageDto;
import com.tamamhuda.minimart.domain.entity.Category;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface CategoryService {
    Category getById(UUID categoryId);

    Category getByName(String name);

    Category getByIdOrName(String IdOrName);

    CategoryDto create(CategoryRequestDto request);

    CategoryDto update(CategoryRequestDto request, UUID categoryId);

    void delete(UUID categoryId);

    PageDto<CategoryDto> getAllCategories(Pageable pageable);
}
