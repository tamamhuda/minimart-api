package com.tamamhuda.minimart.application.service.impl;

import com.tamamhuda.minimart.application.dto.CartDto;
import com.tamamhuda.minimart.application.dto.CartItemDto;
import com.tamamhuda.minimart.application.dto.CartItemRequestDto;
import com.tamamhuda.minimart.application.mapper.CartItemMapper;
import com.tamamhuda.minimart.application.mapper.CartItemRequestMapper;
import com.tamamhuda.minimart.application.mapper.CartMapper;
import com.tamamhuda.minimart.application.service.CartService;
import com.tamamhuda.minimart.domain.entity.Cart;
import com.tamamhuda.minimart.domain.entity.CartItem;
import com.tamamhuda.minimart.domain.entity.Product;
import com.tamamhuda.minimart.domain.entity.User;
import com.tamamhuda.minimart.domain.repository.CartItemRepository;
import com.tamamhuda.minimart.domain.repository.CartRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class CartServiceImpl implements CartService {

    private final CartItemRequestMapper cartItemRequestMapper;
    private final CartMapper cartMapper;
    private final CartItemMapper cartItemMapper;
    private final CartItemRepository cartItemRepository;
    private final CartRepository cartRepository;
    private final ProductServiceImpl productService;

    @Override
    public CartItemDto getCartItem(UUID cartItemId) {
        CartItem cartItem = getCartItemById(cartItemId);
        return cartItemMapper.toDto(cartItem);
    }

    @Override
    public void removeCartItems(List<CartItem> cartItems) {
        cartItemRepository.deleteAllInBatch(cartItems);
    }


    private Cart createCart(User user) {
        Cart cart = new Cart();
        cart.setUser(user);
        return cartRepository.save(cart);
    }

    private CartItem createCartItem(CartItemRequestDto request, Cart cart) {
        CartItem cartItem = cartItemRequestMapper.toEntity(request);
        Product product = productService.findById(UUID.fromString(request.getProductId()));
        cartItem.setProduct(product);
        cartItem.setCart(cart);

        return cartItemRepository.save(cartItem);
    }

    @Override
    public List<CartItem> getCartItemsByIds(List<UUID> cartItemsIds) {
        List<CartItem> cartItems = new ArrayList<>();
        for (UUID cartItemId : cartItemsIds) {
            CartItem cartItem = getCartItemById(cartItemId);
            cartItems.add(cartItem);
        }
        return cartItems;
    }

    private Optional<CartItem> getCartItemByProductId(UUID productId) {
        return cartItemRepository.findByProductId(productId);
    }

    public CartItem getCartItemById(UUID cartId) {
        return cartItemRepository.findById(cartId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cart Item Not Found")
        );
    }


    private CartItem getOrCreateCartItem(CartItemRequestDto request, Cart cart) {
        UUID productId = UUID.fromString(request.getProductId());
        Optional<CartItem> cartItem = getCartItemByProductId(productId);
        if (cartItem.isEmpty()) {
            cartItem = Optional.of(createCartItem(request, cart));
        }
        return cartItem.get();

    }

    private CartItem getOrUpdateCartItem(CartItemRequestDto request, UUID cartItemId) {
        CartItem cartItem = getCartItemById(cartItemId);
        cartItemRequestMapper.updateFromRequestDto(request, cartItem);
        cartItem.setUpdatedAt(Instant.now());
        return cartItemRepository.save(cartItem);
    }

    private Cart getOrCreateCart(User user) {
        Optional<Cart> cart = cartRepository.findByUserId(user.getId());

        if (cart.isEmpty()) {
            cart = Optional.of(createCart(user));
        }

        return cart.get();
    }

    private CartItem saveCartItem(Cart cart, CartItem item) {
        cart.AddItem(item);
        cart.setUpdatedAt(Instant.now());
        cartRepository.save(cart);
        return item;
    }

    @Override
    public CartItemDto addCartItem(User user, CartItemRequestDto request) {
        Cart cart = getOrCreateCart(user);
        CartItem cartItem = getOrCreateCartItem(request, cart);

        CartItem savedItem = saveCartItem(cart, cartItem);

        return cartItemMapper.toDto(savedItem);
    }

    @Override
    public void removeCartItem(User user, UUID cartItemId) {
        Cart userCart = getOrCreateCart(user);
        CartItem cartItem = getCartItemById(cartItemId);

        userCart.RemoveItem(cartItem);
        cartItem.setUpdatedAt(Instant.now());
        cartRepository.save(userCart);

        cartItemRepository.delete(cartItem);
    }

    @Override
    public CartItemDto updateCartItem(UUID cartItemId, CartItemRequestDto request) {
        CartItem cartItem = getOrUpdateCartItem(request, cartItemId);

        return cartItemMapper.toDto(cartItem);
    }

    @Override
    public CartDto getCart(User user, Pageable pageable) {
        Cart cart = getOrCreateCart(user);
        Page<CartItemDto> page = cartItemRepository.findAllByCart(cart, pageable).map(cartItemMapper::toDto);
        return cartMapper.toDtoWithPageItems(cart, page);
    }
}
