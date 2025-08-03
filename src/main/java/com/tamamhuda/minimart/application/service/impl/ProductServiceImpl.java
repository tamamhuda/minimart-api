package com.tamamhuda.minimart.application.service.impl;

import com.tamamhuda.minimart.application.dto.ProductDto;
import com.tamamhuda.minimart.application.dto.ProductRequestDto;
import com.tamamhuda.minimart.application.mapper.ProductMapper;
import com.tamamhuda.minimart.application.service.ProductService;
import com.tamamhuda.minimart.domain.entity.Category;
import com.tamamhuda.minimart.domain.entity.Product;
import com.tamamhuda.minimart.domain.repository.ProductRepository;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final CategoryServiceImpl categoryService;
    private final S3ServiceImpl s3Service;

    @Override
    public ResponseEntity<ProductDto> create(ProductRequestDto request) {
        Product requestProduct = productMapper.toEntity(request);

        Category category = categoryService.getByIdOrName(request.getCategoryIdOrName());

        requestProduct.setCategory(category);
        Product product = productRepository.save(requestProduct);

        return ResponseEntity.status(HttpStatus.CREATED).body(productMapper.toResponseDto(product));
    }

    @Override
    public ResponseEntity<ProductDto> update(ProductRequestDto request, UUID productId) {
        Product product = findById(productId);

        productMapper.updateFromDto(request, product);

        if (request.getCategoryIdOrName() != null) {
            Category category = categoryService.getByIdOrName(request.getCategoryIdOrName());
            product.setCategory(category);
        }

        product.setUpdatedAt(Instant.now());

        Product updatedProduct = productRepository.save(product);

        return ResponseEntity.status(HttpStatus.OK).body(productMapper.toResponseDto(updatedProduct));
    }

    @Override
    public ResponseEntity<?> deleteProductById(UUID productId) {
        Product product = findById(productId);
        productRepository.delete(product);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    public Product findById(UUID id) {
        return productRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found")
        );
    }

    public ResponseEntity<ProductDto> getProductById(UUID id) {
        Product product = findById(id);

        return ResponseEntity.status(HttpStatus.OK).body(productMapper.toResponseDto(product));
    }

    @Override
    public ResponseEntity<List<ProductDto>> getAllProducts(String categoryIdOrName) {
        List<Product> products;

        if (categoryIdOrName != null) {
            return getByCategory(categoryIdOrName);
        }

        products = productRepository.findAll();

        return ResponseEntity.status(HttpStatus.OK).body((productMapper.toResponseDto(products)));
    }

    @Override
    public ResponseEntity<List<ProductDto>> getByCategory(String categoryIdOrName) {
        Category category = categoryService.getByIdOrName(categoryIdOrName);

        List<Product> products = productRepository.findByCategoryId(category.getId());

        return ResponseEntity.status(HttpStatus.OK).body((productMapper.toResponseDto(products)));
    }

    @Override
    public ResponseEntity<ProductDto> uploadProductImage(MultipartFile file, UUID productId) {
        Product product = findById(productId);

        String imageUrl = s3Service.uploadImage(file, "products");
        product.setImageUrl(imageUrl);
        product.setUpdatedAt(Instant.now());
        productRepository.save(product);

        return ResponseEntity.status(HttpStatus.OK).body(productMapper.toResponseDto(product));
    }

    private Product validateProductImageUrl(UUID productId, String imageUrl) {
        Product product = findById(productId);

        if (product.getImageUrl() == null || !product.getImageUrl().equals(imageUrl)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Ensure product image exists");
        }

        return product;
    }

    @Override
    public void proxyProductImage(HttpServletResponse response, UUID productId, String imageUrl) {
        Product product = validateProductImageUrl(productId, imageUrl);

        s3Service.proxyImage(response, "products", product.getImageUrl());
    }

    @Override
    public ResponseEntity<Page<Product>> getProductByFilters(String categoryIdOrName, BigDecimal minPrice, BigDecimal maxPrice, Pageable pageable) {
        String categoryId = null;

        if (categoryIdOrName != null) {
            Category category = categoryService.getByIdOrName(categoryIdOrName);
            categoryId = category.getId().toString();
        }

        Page<Product> products = productRepository.findByAllFilters(categoryId, minPrice, maxPrice, pageable);

        return ResponseEntity.status(HttpStatus.OK).body(products);
    }
}
