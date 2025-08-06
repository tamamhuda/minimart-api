package com.tamamhuda.minimart.api.v1.controller;

import com.tamamhuda.minimart.application.dto.OrderDto;
import com.tamamhuda.minimart.application.dto.OrderRequestDto;
import com.tamamhuda.minimart.application.service.impl.OrderServiceImpl;
import com.tamamhuda.minimart.common.annotation.CurrentUser;
import com.tamamhuda.minimart.common.annotation.RequiredRoles;
import com.tamamhuda.minimart.domain.entity.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
@Validated
public class OrderController {

    private final OrderServiceImpl orderService;

    @PostMapping("/checkout")
    @PreAuthorize("hasRole('CUSTOMER')")
    @RequiredRoles({"CUSTOMER"})
    public ResponseEntity<OrderDto> checkout(@CurrentUser User user, @Valid @RequestBody OrderRequestDto request) {
        return orderService.checkout(user, request);
    }

    @GetMapping
    @PreAuthorize("hasRole('CUSTOMER')")
    @RequiredRoles({"CUSTOMER"})
    public ResponseEntity<List<OrderDto>> getAllUserOrders(@CurrentUser User user) {
        return orderService.getAllUserOrders(user);
    }

    @GetMapping("/{order_id}")
    @PreAuthorize("hasRole('CUSTOMER')")
    @RequiredRoles({"CUSTOMER"})
    public ResponseEntity<OrderDto> getOrderById(@PathVariable("order_id") UUID orderId) {
        return orderService.getOrderById(orderId);
    }


}
