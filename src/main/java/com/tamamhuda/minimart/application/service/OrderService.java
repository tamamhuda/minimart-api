package com.tamamhuda.minimart.application.service;

import com.tamamhuda.minimart.application.dto.OrderDto;
import com.tamamhuda.minimart.application.dto.OrderRequestDto;
import com.tamamhuda.minimart.domain.entity.Order;
import com.tamamhuda.minimart.domain.entity.User;

import java.util.List;
import java.util.UUID;

public interface OrderService {

    OrderDto checkout(User user, OrderRequestDto request);

    List<OrderDto> getAllUserOrders(User user);

    OrderDto getOrderById(UUID orderId);

    Order findById(UUID orderId);

}
