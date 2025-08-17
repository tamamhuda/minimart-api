package com.tamamhuda.minimart.application.service;

import com.tamamhuda.minimart.application.dto.CartDto;
import com.tamamhuda.minimart.application.dto.CartItemDto;
import com.tamamhuda.minimart.application.dto.CartItemRequestDto;
import com.tamamhuda.minimart.domain.entity.CartItem;
import com.tamamhuda.minimart.domain.entity.User;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface CartService {
    CartItemDto addCartItem(User user, CartItemRequestDto request);

    CartItem getCartItemById(UUID cartId);

    List<CartItem> getCartItemsByIds(List<UUID> cartItemsIds);

    void removeCartItem(User user, UUID cartItemId);

    CartItemDto updateCartItem(UUID cartItemId, CartItemRequestDto request);

    CartDto getCart(User user, Pageable pageable);

    CartItemDto getCartItem(UUID cartItemId);

    void removeCartItems(List<CartItem> cartItems);
}
