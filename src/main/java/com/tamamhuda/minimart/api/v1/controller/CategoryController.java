package com.tamamhuda.minimart.api.v1.controller;

import com.tamamhuda.minimart.application.dto.CategoryDto;
import com.tamamhuda.minimart.application.dto.CategoryRequestDto;
import com.tamamhuda.minimart.application.service.impl.CategoryServiceImpl;
import com.tamamhuda.minimart.common.annotation.RequiredRoles;
import com.tamamhuda.minimart.common.validation.group.Create;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
@Validated
public class CategoryController {
    private final CategoryServiceImpl categoryService;

    @PostMapping()
    @PreAuthorize("hasRole('ADMIN')")
    @RequiredRoles({"ADMIN"})
    public ResponseEntity<CategoryDto> create(@Validated(Create.class) @RequestBody CategoryRequestDto request) {
        return categoryService.create(request);
    }

    @PutMapping("/{category_id}")
    @RequiredRoles({"ADMIN"})
    public ResponseEntity<CategoryDto> update(@Valid @RequestBody CategoryRequestDto request, @PathVariable("category_id") UUID categoryId) {
        return categoryService.update(request, categoryId);
    }

    @DeleteMapping("/{category_id}")
    @RequiredRoles({"ADMIN"})
    public ResponseEntity<?> delete(@PathVariable("category_id") UUID categoryId) {
        return categoryService.delete(categoryId);
    }

    @GetMapping()
    public ResponseEntity<List<CategoryDto>> getAllCategories() {
        return categoryService.getAllCategories();
    }
}
