package com.tamamhuda.minimart.api.v1.controller;


import com.tamamhuda.minimart.application.dto.ProductDto;
import com.tamamhuda.minimart.application.dto.ProductRequestDto;
import com.tamamhuda.minimart.application.schema.ApiResponsePageProductSchema;
import com.tamamhuda.minimart.application.schema.ApiResponseProductSchema;
import com.tamamhuda.minimart.application.service.impl.ProductServiceImpl;
import com.tamamhuda.minimart.common.annotation.ApiNotFoundResponse;
import com.tamamhuda.minimart.common.annotation.ApiUnauthorizedResponse;
import com.tamamhuda.minimart.common.annotation.ApiValidationErrorResponse;
import com.tamamhuda.minimart.common.annotation.RequiredRoles;
import com.tamamhuda.minimart.common.dto.ErrorResponseDto;
import com.tamamhuda.minimart.common.dto.PageDto;
import com.tamamhuda.minimart.common.validation.group.Create;
import com.tamamhuda.minimart.common.validation.group.Update;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.UUID;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
@Validated
@Tag(
        name = "Product Management",
        description = "Manage all products"
)
public class ProductController {
    private final ProductServiceImpl productService;


    @PostMapping()
    @Validated(Create.class)
    @PreAuthorize("hasRole('ADMIN')")
    @RequiredRoles({"ADMIN"})
    @SecurityRequirement(name = "AuthorizationHeader")
    @Operation(
            summary = "Create new product",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Successful Response",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ApiResponseProductSchema.class)
                            )
                    )
            }
    )
    @ApiUnauthorizedResponse()
    @ApiValidationErrorResponse()
    public ResponseEntity<ProductDto> create(@Validated(Create.class) @RequestBody ProductRequestDto request){
        ProductDto product = productService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(product);
    }

    @GetMapping("/{product_id}")
    @Operation(
            summary = "Get product by ID",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successful Response",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ApiResponseProductSchema.class)
                            )
                    )
            }
    )
    @ApiValidationErrorResponse()
    @ApiNotFoundResponse(description = "Product not found", message = "Product not found")
    public ResponseEntity<ProductDto> getProductById(@PathVariable("product_id") UUID productId){
        ProductDto productDto = productService.getProductById(productId);
        return ResponseEntity.status(HttpStatus.CREATED).body(productDto);
    }

    @PutMapping("/{product_id}")
    @PreAuthorize("hasRole('ADMIN')")
    @RequiredRoles({"ADMIN"})
    @SecurityRequirement(name = "AuthorizationHeader")
    @Operation(
            summary = "Update product",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successful Response",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ApiResponseProductSchema.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Invalid or expired token",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponseDto.class)
                            )
                    )
            }
    )
    @ApiUnauthorizedResponse()
    @ApiValidationErrorResponse()
    @ApiNotFoundResponse(description = "Product not found", message = "Product not found")
    public ResponseEntity<ProductDto> update(@Validated(Update.class) @RequestBody ProductRequestDto request, @PathVariable UUID product_id){
        ProductDto product =  productService.update(request, product_id);
        return ResponseEntity.status(HttpStatus.OK).body(product);
    }

    @DeleteMapping("/{product_id}")
    @PreAuthorize("hasRole('ADMIN')")
    @RequiredRoles({"ADMIN"})
    @SecurityRequirement(name = "AuthorizationHeader")
    @Operation(
            summary = "Delete product",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successful Response",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ApiResponseProductSchema.class)
                            )
                    )
            }
    )
    @ApiUnauthorizedResponse()
    @ApiValidationErrorResponse()
    @ApiNotFoundResponse(description = "Product not found", message = "Product not found")
    public ResponseEntity<?> deleteProductById(@PathVariable("product_id") UUID productId){
        productService.deleteProductById(productId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping()
    @Operation(
            summary = "Get all products with filters",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successful Response",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ApiResponsePageProductSchema.class)
                            )
                    )
            }
    )
    @ApiValidationErrorResponse()
    public ResponseEntity<PageDto<ProductDto>> getProductByAllFilters(
            @RequestParam(name = "category", required = false) String category,
            @RequestParam(name = "minPrice", required = false) BigDecimal minPrice,
            @RequestParam(name = "maxPrice", required = false) BigDecimal maxPrice,
            Pageable pageable
            ){

        PageDto<ProductDto> products = productService.getProductsByFilters(category, minPrice, maxPrice, pageable);

        return ResponseEntity.status(HttpStatus.OK).body(products);
    }

    @PostMapping(value = "/{product_id}/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('ADMIN')")
    @RequiredRoles({"ADMIN"})
    @SecurityRequirement(name = "AuthorizationHeader")
    @Operation(
            summary = "Upload product image",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successful Response",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ApiResponseProductSchema.class)
                            )
                    )
            }
    )
    @ApiUnauthorizedResponse()
    @ApiValidationErrorResponse()
    @ApiNotFoundResponse(description = "Product not found", message = "Product not found")
    public ResponseEntity<ProductDto> uploadProductImage(@PathVariable("product_id") UUID productId, @RequestPart("file") MultipartFile file){
        ProductDto product = productService.uploadProductImage(file, productId);
        return ResponseEntity.status(HttpStatus.OK).body(product);
    }

    @GetMapping("/{product_id}/images/{imageUrl}")
    @Operation(
            summary = "View product image",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successful Response",
                            content = @Content(
                                    mediaType = "image/png"
                            )
                    ),

            }
    )
    @ApiNotFoundResponse(description = "Product Image not found", message = "Product Image not found")
    public void proxyProductImage(@PathVariable("product_id") UUID productId, @PathVariable("imageUrl") String imageUrl, HttpServletResponse response){
        productService.proxyProductImage(response, productId, imageUrl);
    }

}
