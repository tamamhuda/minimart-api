package com.tamamhuda.minimart.application.service.impl;

import com.tamamhuda.minimart.application.service.CategoryService;
import com.tamamhuda.minimart.domain.entity.Category;
import com.tamamhuda.minimart.domain.repository.CategoryRepository;
import lombok.AllArgsConstructor;
import org.aspectj.apache.bcel.generic.RET;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;

    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

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
}
