package com.tamamhuda.minimart.application.service;

import com.tamamhuda.minimart.domain.entity.Category;

import java.util.List;
import java.util.UUID;

public interface CategoryService {
    public List<Category> getAllCategories();

    public Category getById(UUID categoryId);

    public Category getByName(String name);

    public Category getByIdOrName(String IdOrName);

}
