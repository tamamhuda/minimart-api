package com.tamamhuda.minimart.application.service;

import com.tamamhuda.minimart.application.dto.OrderDto;
import com.tamamhuda.minimart.application.dto.OrderRequestDto;
import com.tamamhuda.minimart.domain.entity.User;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

public interface OrderService {

    ResponseEntity<OrderDto> checkout(User user, OrderRequestDto request);

    ResponseEntity<List<OrderDto>> getAllUserOrders(User user);

    ResponseEntity<OrderDto> getOrderById(UUID orderId);

}
