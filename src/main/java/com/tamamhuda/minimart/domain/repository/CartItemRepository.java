package com.tamamhuda.minimart.domain.repository;

import com.tamamhuda.minimart.domain.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CartItemRepository extends JpaRepository<CartItem, UUID> {
}
