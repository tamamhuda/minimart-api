package com.tamamhuda.minimart.application.service.impl;

import com.tamamhuda.minimart.application.dto.ProductDto;
import com.tamamhuda.minimart.application.dto.ProductRequestDto;
import com.tamamhuda.minimart.application.mapper.ProductMapper;
import com.tamamhuda.minimart.application.mapper.ProductRequestMapper;
import com.tamamhuda.minimart.application.service.ProductService;
import com.tamamhuda.minimart.common.dto.PageDto;
import com.tamamhuda.minimart.domain.entity.Category;
import com.tamamhuda.minimart.domain.entity.Product;
import com.tamamhuda.minimart.domain.repository.ProductRepository;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
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
    private final ProductRequestMapper productRequestMapper;

    @Override
    public ProductDto create(ProductRequestDto request) {
        Product requestProduct = productRequestMapper.toEntity(request);

        Category category = categoryService.getByIdOrName(request.getCategoryIdOrName());

        requestProduct.setCategory(category);
        Product product = productRepository.save(requestProduct);

        return productMapper.toDto(product);
    }

    @Override
    @CachePut(cacheNames = "product", key = "#productId")
    @CacheEvict(cacheNames = "productsFilter", allEntries = true)
    public ProductDto update(ProductRequestDto request, UUID productId) {
        Product product = findById(productId);

        productRequestMapper.updateFromRequestDto(request, product);

        if (request.getCategoryIdOrName() != null) {
            Category category = categoryService.getByIdOrName(request.getCategoryIdOrName());
            product.setCategory(category);
        }

        product.setUpdatedAt(Instant.now());

        Product updatedProduct = productRepository.save(product);

        return productMapper.toDto(updatedProduct);
    }
    @Override
    @Caching(
            evict = {
                    @CacheEvict(cacheNames = "product", key = "#productId"),
                    @CacheEvict(cacheNames = "productsFilters", allEntries = true )
            }
    )
    public void deleteProductById(UUID productId) {
        Product product = findById(productId);
        productRepository.delete(product);
    }

    public Product findById(UUID id) {
        return productRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found")
        );
    }

    @Override
    @Cacheable(cacheNames = "product", key = "#productId")
    public ProductDto getProductById(UUID productId) {
        Product product = findById(productId);

        return productMapper.toDto(product);
    }

    @Override
    public List<ProductDto> getByCategory(String categoryIdOrName) {
        Category category = categoryService.getByIdOrName(categoryIdOrName);

        List<Product> products = productRepository.findByCategoryId(category.getId());

        return productMapper.toDto(products);
    }

    @Override
    @CachePut(cacheNames = "product", key = "#productId")
    @CacheEvict(cacheNames = "productsFilters", allEntries = true )
    public ProductDto uploadProductImage(MultipartFile file, UUID productId) {
        Product product = findById(productId);

        String imageUrl = s3Service.uploadImage(file, "products");
        product.setImageUrl(imageUrl);
        product.setUpdatedAt(Instant.now());
        productRepository.save(product);

        return productMapper.toDto(product);
    }

    private Product validateProductImageUrl(UUID productId, String imageUrl) {
        Product product = findById(productId);

        if (product.getImageUrl() == null || !product.getImageUrl().equals(imageUrl)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Ensure product image exists");
        }

        return product;
    }

    @Override
    @CachePut(cacheNames = "product", key = "#productId")
    public void proxyProductImage(HttpServletResponse response, UUID productId, String imageUrl) {
        Product product = validateProductImageUrl(productId, imageUrl);
        s3Service.proxyImage(response, "products", product.getImageUrl());
    }

    @Override
    @Cacheable(
            cacheNames = "productsFilters",
            key = "new org.springframework.cache.interceptor.SimpleKey(#categoryIdOrName, #minPrice, #maxPrice, #pageable.pageNumber, #pageable.pageSize, #pageable.sort.toString())"
    )
    public PageDto<ProductDto> getProductsByFilters(String categoryIdOrName, BigDecimal minPrice, BigDecimal maxPrice, Pageable pageable) {
        String categoryId = null;

        if (categoryIdOrName != null) {
            Category category = categoryService.getByIdOrName(categoryIdOrName);
            categoryId = category.getId().toString();
        }

        Page<Product> productPage = productRepository.findByAllFilters(categoryId, minPrice, maxPrice, pageable);

        Page<ProductDto> page = productPage.map(productMapper::toDto);

        return PageDto.<ProductDto>builder()
                .content(page.getContent())
                .pageNumber(page.getNumber())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .pageSize(page.getSize())
                .last(page.isLast())
                .build();
    }
}
