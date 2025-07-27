package com.tamamhuda.minimart.domain.repository;

import com.tamamhuda.minimart.domain.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, UUID> {
}
