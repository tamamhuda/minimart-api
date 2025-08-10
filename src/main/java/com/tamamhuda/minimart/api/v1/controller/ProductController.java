package com.tamamhuda.minimart.api.v1.controller;


import com.tamamhuda.minimart.application.dto.ProductDto;
import com.tamamhuda.minimart.application.dto.ProductRequestDto;
import com.tamamhuda.minimart.application.service.impl.ProductServiceImpl;
import com.tamamhuda.minimart.common.annotation.RequiredRoles;
import com.tamamhuda.minimart.common.dto.PageResponse;
import com.tamamhuda.minimart.common.validation.group.Create;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
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
        ProductDto product = productService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(product);
    }

    @GetMapping("/{product_id}")
    public ResponseEntity<ProductDto> getProductById(@PathVariable("product_id") UUID productId){
        ProductDto productDto = productService.getProductById(productId);
        return ResponseEntity.status(HttpStatus.CREATED).body(productDto);
    }

    @PutMapping("/{product_id}")
    @PreAuthorize("hasRole('ADMIN')")
    @RequiredRoles({"ADMIN"})
    public ResponseEntity<ProductDto> update(@Valid @RequestBody ProductRequestDto request, @PathVariable UUID product_id){
        ProductDto product =  productService.update(request, product_id);
        return ResponseEntity.status(HttpStatus.OK).body(product);
    }

    @DeleteMapping("/{product_id}")
    @PreAuthorize("hasRole('ADMIN')")
    @RequiredRoles({"ADMIN"})
    public ResponseEntity<?> deleteProductById(@PathVariable("product_id") UUID productId){
        productService.deleteProductById(productId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping()
    public ResponseEntity<PageResponse<ProductDto>> getProductByAllFilters(
            @RequestParam(name = "category", required = false) String category,
            @RequestParam(name = "minPrice", required = false) BigDecimal minPrice,
            @RequestParam(name = "maxPrice", required = false) BigDecimal maxPrice,
            Pageable pageable
            ){

        PageResponse<ProductDto> products = productService.getProductsByFilters(category, minPrice, maxPrice, pageable);

        return ResponseEntity.status(HttpStatus.OK).body(products);
    }

    @PostMapping("/{product_id}/upload")
    @PreAuthorize("hasRole('ADMIN')")
    @RequiredRoles({"ADMIN"})
    public ResponseEntity<ProductDto> uploadProductImage(@PathVariable("product_id") UUID productId, @RequestParam("file") MultipartFile file){
        ProductDto product = productService.uploadProductImage(file, productId);
        return ResponseEntity.status(HttpStatus.OK).body(product);
    }

    @GetMapping("/{product_id}/images/{imageUrl}")
    public void proxyProductImage(@PathVariable("product_id") UUID productId, @PathVariable("imageUrl") String imageUrl, HttpServletResponse response){
        productService.proxyProductImage(response, productId, imageUrl);
    }

}
