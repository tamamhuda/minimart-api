package com.tamamhuda.minimart.application.service;

import com.tamamhuda.minimart.application.dto.CartDto;
import com.tamamhuda.minimart.application.dto.CartItemDto;
import com.tamamhuda.minimart.application.dto.CartItemRequestDto;
import com.tamamhuda.minimart.domain.entity.User;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

public interface CartService {
    ResponseEntity<CartDto> addCartItem(User user, CartItemRequestDto request);

    ResponseEntity<CartDto> removeCartItem(User user, UUID cartItemId);

    ResponseEntity<CartItemDto> updateCartItem(UUID cartItemId, CartItemRequestDto request);

    ResponseEntity<CartDto> getCart(User user);

    ResponseEntity<CartItemDto> getCartItem(UUID cartItemId);
}
