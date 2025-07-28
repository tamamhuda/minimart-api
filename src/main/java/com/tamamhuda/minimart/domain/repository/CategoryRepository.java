package com.tamamhuda.minimart.domain.repository;

import com.tamamhuda.minimart.domain.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CategoryRepository extends JpaRepository<Category, UUID> {
}
