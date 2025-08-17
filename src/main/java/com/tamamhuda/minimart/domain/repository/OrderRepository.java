package com.tamamhuda.minimart.domain.repository;

import com.tamamhuda.minimart.domain.entity.Order;
import com.tamamhuda.minimart.domain.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, UUID> {
    List<Order> findAllByUserId(UUID userId);

    Page<Order> findAllByUser(User user, Pageable pageable);
}
