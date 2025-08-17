package com.tamamhuda.minimart.api.v1.controller;

import com.tamamhuda.minimart.application.dto.CartDto;
import com.tamamhuda.minimart.application.dto.CartItemDto;
import com.tamamhuda.minimart.application.dto.CartItemRequestDto;
import com.tamamhuda.minimart.application.schema.ApiResponseCartItemSchema;
import com.tamamhuda.minimart.application.schema.ApiResponseCartSchema;
import com.tamamhuda.minimart.application.service.impl.CartServiceImpl;
import com.tamamhuda.minimart.common.annotation.*;
import com.tamamhuda.minimart.common.validation.group.Create;
import com.tamamhuda.minimart.common.validation.group.Update;
import com.tamamhuda.minimart.domain.entity.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
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
@Tag(
        name = "Cart Management",
        description = "Manage all cart and cart item of user"
)
@SecurityRequirement(name = "AuthorizationHeader")
public class CartController {
    private final CartServiceImpl cartService;

    @PostMapping("/items")
    @PreAuthorize("hasRole('CUSTOMER')")
    @RequiredRoles({"CUSTOMER"})
    @Operation(
            summary = "Add items to cart",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Successful Response",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ApiResponseCartItemSchema.class)

                            )
                    )
            }
    )
    @ApiUnauthorizedResponse()
    @ApiValidationErrorResponse()
    @ApiNotFoundResponse(description = "Product not found", message = "Product not found")
    public ResponseEntity<CartItemDto> addItemToCart(@CurrentUser User user, @Validated(Create.class) @RequestBody CartItemRequestDto request) {
        CartItemDto response = cartService.addCartItem(user, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/items/{item_id}")
    @PreAuthorize("hasRole('CUSTOMER')")
    @RequiredRoles({"CUSTOMER"})
    @Operation(
            summary = "Get cart item by ID",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successful Response",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ApiResponseCartItemSchema.class)

                            )
                    )
            }
    )
    @ApiUnauthorizedResponse()
    @ApiValidationErrorResponse()
    @ApiNotFoundResponse(description = "Cart item not found", message = "Cart item not found")
    public ResponseEntity<CartItemDto> getCartItem(@PathVariable("item_id") UUID item_id) {
        CartItemDto response = cartService.getCartItem(item_id);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/items/{item_id}")
    @PreAuthorize("hasRole('CUSTOMER')")
    @RequiredRoles({"CUSTOMER"})
    @Operation(
            summary = "Delete item from cart",
            responses = {
                    @ApiResponse(
                            responseCode = "204",
                            description = "Successful Response",
                            content = @Content(
                                    mediaType = "application/json"

                            )
                    )
            }
    )
    @ApiUnauthorizedResponse()
    @ApiValidationErrorResponse()
    @ApiNotFoundResponse(description = "Cart item not found", message = "Cart item not found")
    public ResponseEntity<?> removeCartItem(@CurrentUser User user, @PathVariable("item_id") UUID item_id) {
        cartService.removeCartItem(user, item_id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PutMapping("/items/{item_id}")
    @PreAuthorize("hasRole('CUSTOMER')")
    @RequiredRoles({"CUSTOMER"})
    @Operation(
            summary = "Update cart item",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successful Response",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ApiResponseCartItemSchema.class)

                            )
                    )
            }
    )
    @ApiUnauthorizedResponse()
    @ApiValidationErrorResponse()
    @ApiNotFoundResponse(description = "Cart item not found", message = "Cart item not found")
    public ResponseEntity<CartItemDto> updateCartItem(@Validated(Update.class) @RequestBody CartItemRequestDto request, @PathVariable("item_id") UUID item_id) {
        CartItemDto response = cartService.updateCartItem(item_id, request);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping()
    @PreAuthorize("hasRole('CUSTOMER')")
    @RequiredRoles({"CUSTOMER"})
    @Operation(
            summary = "Get detail user cart including cart items",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successful Response",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ApiResponseCartSchema.class)

                            )
                    )
            }
    )
    @ApiUnauthorizedResponse()
    @ApiValidationErrorResponse()
    public ResponseEntity<CartDto> getCart(@CurrentUser User user, Pageable pageable) {
        CartDto response = cartService.getCart(user, pageable);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
