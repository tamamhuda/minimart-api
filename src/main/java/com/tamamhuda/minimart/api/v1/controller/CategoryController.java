package com.tamamhuda.minimart.api.v1.controller;

import com.tamamhuda.minimart.application.dto.CategoryDto;
import com.tamamhuda.minimart.application.dto.CategoryRequestDto;
import com.tamamhuda.minimart.application.schema.ApiResponseCategorySchema;
import com.tamamhuda.minimart.application.schema.ApiResponsePageCategorySchema;
import com.tamamhuda.minimart.application.service.impl.CategoryServiceImpl;
import com.tamamhuda.minimart.common.annotation.ApiNotFoundResponse;
import com.tamamhuda.minimart.common.annotation.ApiUnauthorizedResponse;
import com.tamamhuda.minimart.common.annotation.ApiValidationErrorResponse;
import com.tamamhuda.minimart.common.annotation.RequiredRoles;
import com.tamamhuda.minimart.common.dto.PageDto;
import com.tamamhuda.minimart.common.validation.group.Create;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
@Validated
@Tag(
        name = "Category Management",
        description = "Manage all product categories"
)
public class CategoryController {
    private final CategoryServiceImpl categoryService;

    @PostMapping()
    @PreAuthorize("hasRole('ADMIN')")
    @RequiredRoles({"ADMIN"})
    @SecurityRequirement(name = "AuthorizationHeader")
    @Operation(summary = "Create category",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Successful response",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ApiResponseCategorySchema.class)
                            ))
            })
    @ApiUnauthorizedResponse()
    @ApiValidationErrorResponse()
    public ResponseEntity<CategoryDto> create(@Validated(Create.class) @RequestBody CategoryRequestDto request) {
        CategoryDto response = categoryService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{category_id}")
    @PreAuthorize("hasRole('ADMIN')")
    @RequiredRoles({"ADMIN"})
    @SecurityRequirement(name = "AuthorizationHeader")
    @Operation(summary = "Update category",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successful response",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ApiResponseCategorySchema.class)
                            ))
            })
    @ApiUnauthorizedResponse()
    @ApiValidationErrorResponse()
    @ApiNotFoundResponse(description = "Category not found", message = "Category not found")
    public ResponseEntity<CategoryDto> update(@Valid @RequestBody CategoryRequestDto request, @PathVariable("category_id") UUID categoryId) {
        CategoryDto response = categoryService.update(request, categoryId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/{category_id}")
    @PreAuthorize("hasRole('ADMIN')")
    @RequiredRoles({"ADMIN"})
    @SecurityRequirement(name = "AuthorizationHeader")
    @Operation(summary = "Delete category",
            responses = {
                    @ApiResponse(
                            responseCode = "204",
                            description = "Successful response",
                            content = @Content(
                                    mediaType = "application/json"
                            ))
            })
    @ApiUnauthorizedResponse()
    @ApiValidationErrorResponse()
    @ApiNotFoundResponse(description = "Category not found", message = "Category not found")
    public ResponseEntity<?> delete(@PathVariable("category_id") UUID categoryId) {
        categoryService.delete(categoryId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping()
    @Operation(summary = "Get all categories",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successful response",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ApiResponsePageCategorySchema.class)
                            ))
            })
    public ResponseEntity<PageDto<CategoryDto>> getAllCategories(Pageable pageable) {
        PageDto<CategoryDto> response = categoryService.getAllCategories(pageable);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
