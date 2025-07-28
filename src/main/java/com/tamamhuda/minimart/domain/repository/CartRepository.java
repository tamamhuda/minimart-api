package com.tamamhuda.minimart.domain.repository;

import com.tamamhuda.minimart.domain.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CartRepository extends JpaRepository<Cart, UUID> {
}
