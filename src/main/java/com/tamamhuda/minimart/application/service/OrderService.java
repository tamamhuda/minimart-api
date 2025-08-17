package com.tamamhuda.minimart.application.service;

import com.tamamhuda.minimart.application.dto.OrderDto;
import com.tamamhuda.minimart.application.dto.OrderRequestDto;
import com.tamamhuda.minimart.common.dto.PageDto;
import com.tamamhuda.minimart.domain.entity.Order;
import com.tamamhuda.minimart.domain.entity.User;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface OrderService {

    OrderDto checkout(User user, OrderRequestDto request);

    PageDto<OrderDto> getAllUserOrders(User user, Pageable pageable);

    OrderDto getOrderById(UUID orderId);

    Order findById(UUID orderId);

}
