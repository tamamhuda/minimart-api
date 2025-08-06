package com.tamamhuda.minimart.api.v1.controller;


import com.tamamhuda.minimart.application.dto.ProductDto;
import com.tamamhuda.minimart.application.dto.ProductRequestDto;
import com.tamamhuda.minimart.application.service.impl.ProductServiceImpl;
import com.tamamhuda.minimart.common.annotation.RequiredRoles;
import com.tamamhuda.minimart.common.validation.group.Create;
import com.tamamhuda.minimart.domain.entity.Product;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
public class ProductController {
    private final ProductServiceImpl productService;


    @PostMapping()
    @Validated(Create.class)
    @PreAuthorize("hasRole('ADMIN')")
    @RequiredRoles({"ADMIN"})
    public ResponseEntity<ProductDto> create(@Valid @RequestBody ProductRequestDto request){
        return productService.create(request);
    }

    @GetMapping("/{product_id}")
    public ResponseEntity<ProductDto> getProductById(@PathVariable("product_id") UUID productId){
        return productService.getProductById(productId);
    }

    @PutMapping("/{product_id}")
    @PreAuthorize("hasRole('ADMIN')")
    @RequiredRoles({"ADMIN"})
    public ResponseEntity<ProductDto> update(@Valid @RequestBody ProductRequestDto request, @PathVariable UUID product_id){
        return productService.update(request, product_id);
    }

    @DeleteMapping("/{product_id}")
    @PreAuthorize("hasRole('ADMIN')")
    @RequiredRoles({"ADMIN"})
    public ResponseEntity<?> deleteProductById(@PathVariable("product_id") UUID productId){
        return productService.deleteProductById(productId);
    }

    @GetMapping()
    public ResponseEntity<Page<Product>> getProductByAllFilters(
            @RequestParam(name = "category", required = false) String category,
            @RequestParam(name = "minPrice", required = false) BigDecimal minPrice,
            @RequestParam(name = "maxPrice", required = false) BigDecimal maxPrice,
            Pageable pageable
            ){

        return productService.getProductByFilters(category, minPrice, maxPrice, pageable);
    }

    @PostMapping("/{product_id}/upload")
    @PreAuthorize("hasRole('ADMIN')")
    @RequiredRoles({"ADMIN"})
    public ResponseEntity<ProductDto> uploadProductImage(@PathVariable("product_id") UUID productId, @RequestParam("file") MultipartFile file){
        return productService.uploadProductImage(file, productId);
    }

    @GetMapping("/{product_id}/images/{imageUrl}")
    public void proxyProductImage(@PathVariable("product_id") UUID productId, @PathVariable("imageUrl") String imageUrl, HttpServletResponse response){
        productService.proxyProductImage(response, productId, imageUrl);
    }

}
