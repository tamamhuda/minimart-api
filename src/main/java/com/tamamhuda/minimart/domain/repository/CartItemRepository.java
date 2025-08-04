package com.tamamhuda.minimart.domain.repository;

import com.tamamhuda.minimart.domain.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface CartItemRepository extends JpaRepository<CartItem, UUID> {

    Optional<CartItem> findByProductId(UUID productId);
}
