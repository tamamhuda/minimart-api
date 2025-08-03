package com.tamamhuda.minimart.application.service;

import com.tamamhuda.minimart.application.dto.ProductDto;
import com.tamamhuda.minimart.application.dto.ProductRequestDto;
import com.tamamhuda.minimart.domain.entity.Product;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

public interface ProductService {

    public ResponseEntity<ProductDto> create(ProductRequestDto request);

    public ResponseEntity<ProductDto> update(ProductRequestDto request, UUID productId);

    public ResponseEntity<?> deleteProductById(UUID productId);

    public Product findById(UUID productId);

    public ResponseEntity<ProductDto> getProductById(UUID productId);

    public ResponseEntity<List<ProductDto>> getAllProducts(String categoryIdOrName);

    public ResponseEntity<List<ProductDto>> getByCategory(String categoryIdOrName);

    public ResponseEntity<ProductDto> uploadProductImage(MultipartFile file, UUID productId);

    public void proxyProductImage(HttpServletResponse response, UUID productId, String imageUrl);
}
