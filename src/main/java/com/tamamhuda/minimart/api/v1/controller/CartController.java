package com.tamamhuda.minimart.api.v1.controller;

import com.tamamhuda.minimart.application.dto.CartDto;
import com.tamamhuda.minimart.application.dto.CartItemDto;
import com.tamamhuda.minimart.application.dto.CartItemRequestDto;
import com.tamamhuda.minimart.application.service.impl.CartServiceImpl;
import com.tamamhuda.minimart.common.annotation.CurrentUser;
import com.tamamhuda.minimart.common.annotation.RequiredRoles;
import com.tamamhuda.minimart.common.validation.group.Create;
import com.tamamhuda.minimart.common.validation.group.Update;
import com.tamamhuda.minimart.domain.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
@Validated
public class CartController {
    private final CartServiceImpl cartService;

    @PostMapping("/items")
    @PreAuthorize("hasRole('CUSTOMER')")
    @RequiredRoles({"CUSTOMER"})
    public ResponseEntity<CartItemDto> addItemToCart(@CurrentUser User user, @Validated(Create.class) @RequestBody CartItemRequestDto request) {
        CartItemDto response = cartService.addCartItem(user, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/items/{item_id}")
    @PreAuthorize("hasRole('CUSTOMER')")
    @RequiredRoles({"CUSTOMER"})
    public ResponseEntity<CartItemDto> getCartItem(@PathVariable("item_id") UUID item_id) {
        CartItemDto response = cartService.getCartItem(item_id);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/items/{item_id}")
    @PreAuthorize("hasRole('CUSTOMER')")
    @RequiredRoles({"CUSTOMER"})
    public ResponseEntity<?> removeCartItem(@CurrentUser User user, @PathVariable("item_id") UUID item_id) {
        cartService.removeCartItem(user, item_id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PutMapping("/items/{item_id}")
    @PreAuthorize("hasRole('CUSTOMER')")
    @RequiredRoles({"CUSTOMER"})
    public ResponseEntity<CartItemDto> updateCartItem(@Validated(Update.class) @RequestBody CartItemRequestDto request, @PathVariable("item_id") UUID item_id) {
        CartItemDto response = cartService.updateCartItem(item_id, request);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping()
    @PreAuthorize("hasRole('CUSTOMER')")
    @RequiredRoles({"CUSTOMER"})
    public ResponseEntity<CartDto> getCartItem(@CurrentUser User user) {
        CartDto response = cartService.getCart(user);
        return ResponseEntity.status(HttpStatus.OK).body(response);

    }
}
