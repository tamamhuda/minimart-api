package com.tamamhuda.minimart.domain.repository;

import com.tamamhuda.minimart.domain.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface OrderItemRepository extends JpaRepository<OrderItem, UUID> {
}
