package com.tamamhuda.minimart.domain.repository;

import com.tamamhuda.minimart.domain.entity.Category;
import com.tamamhuda.minimart.domain.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, UUID> {
    List<Product> findByCategoryId(UUID categoryId);
}
