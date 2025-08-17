package com.tamamhuda.minimart.application.service;

import com.tamamhuda.minimart.application.dto.ProductDto;
import com.tamamhuda.minimart.application.dto.ProductRequestDto;
import com.tamamhuda.minimart.common.dto.PageDto;
import com.tamamhuda.minimart.domain.entity.Product;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public interface ProductService {

    ProductDto create(ProductRequestDto request);

    ProductDto update(ProductRequestDto request, UUID productId);

    void deleteProductById(UUID productId);

    Product findById(UUID productId);

    ProductDto getProductById(UUID productId);

    List<ProductDto> getByCategory(String categoryIdOrName);

    ProductDto uploadProductImage(MultipartFile file, UUID productId);

    void proxyProductImage(HttpServletResponse response, UUID productId, String imageUrl);

    PageDto<ProductDto> getProductsByFilters(String categoryIdOrName, BigDecimal minPrice, BigDecimal maxPrice, Pageable pageable);
}
