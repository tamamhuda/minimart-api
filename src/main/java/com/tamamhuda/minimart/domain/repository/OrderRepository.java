package com.tamamhuda.minimart.domain.repository;

import com.tamamhuda.minimart.domain.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, UUID> {
}
