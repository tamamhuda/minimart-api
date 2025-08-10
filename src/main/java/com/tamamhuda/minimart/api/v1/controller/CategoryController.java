package com.tamamhuda.minimart.api.v1.controller;

import com.tamamhuda.minimart.application.dto.CategoryDto;
import com.tamamhuda.minimart.application.dto.CategoryRequestDto;
import com.tamamhuda.minimart.application.service.impl.CategoryServiceImpl;
import com.tamamhuda.minimart.common.annotation.RequiredRoles;
import com.tamamhuda.minimart.common.validation.group.Create;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
        CategoryDto response = categoryService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{category_id}")
    @RequiredRoles({"ADMIN"})
    public ResponseEntity<CategoryDto> update(@Valid @RequestBody CategoryRequestDto request, @PathVariable("category_id") UUID categoryId) {
        CategoryDto response = categoryService.update(request, categoryId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/{category_id}")
    @RequiredRoles({"ADMIN"})
    public ResponseEntity<?> delete(@PathVariable("category_id") UUID categoryId) {
        categoryService.delete(categoryId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping()
    public ResponseEntity<List<CategoryDto>> getAllCategories() {
        List<CategoryDto> response = categoryService.getAllCategories();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
