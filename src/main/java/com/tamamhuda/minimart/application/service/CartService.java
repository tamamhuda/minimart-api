package com.tamamhuda.minimart.application.service;

import com.tamamhuda.minimart.application.dto.CartDto;
import com.tamamhuda.minimart.application.dto.CartItemDto;
import com.tamamhuda.minimart.application.dto.CartItemRequestDto;
import com.tamamhuda.minimart.domain.entity.CartItem;
import com.tamamhuda.minimart.domain.entity.User;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

public interface CartService {
    ResponseEntity<CartItemDto> addCartItem(User user, CartItemRequestDto request);

    CartItem getCartItemById(UUID cartId);

    List<CartItem> getCartItemsByIds(List<UUID> cartItemsIds);

    ResponseEntity<CartDto> removeCartItem(User user, UUID cartItemId);

    ResponseEntity<CartItemDto> updateCartItem(UUID cartItemId, CartItemRequestDto request);

    ResponseEntity<CartDto> getCart(User user);

    ResponseEntity<CartItemDto> getCartItem(UUID cartItemId);

    void removeCartItems(List<CartItem> cartItems);
}
