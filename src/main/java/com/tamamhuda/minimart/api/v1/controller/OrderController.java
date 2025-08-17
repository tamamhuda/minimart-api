package com.tamamhuda.minimart.api.v1.controller;

import com.tamamhuda.minimart.application.dto.OrderDto;
import com.tamamhuda.minimart.application.dto.OrderRequestDto;
import com.tamamhuda.minimart.application.schema.ApiResponseOrderSchema;
import com.tamamhuda.minimart.application.schema.ApiResponsePageOrderSchema;
import com.tamamhuda.minimart.application.service.impl.OrderServiceImpl;
import com.tamamhuda.minimart.common.annotation.*;
import com.tamamhuda.minimart.common.dto.PageDto;
import com.tamamhuda.minimart.domain.entity.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
@Validated
@Tag(
        name = "Order Management",
        description = "Manage all user orders"
)
@SecurityRequirement(name = "AuthorizationHeader")
public class OrderController {

    private final OrderServiceImpl orderService;

    @PostMapping("/checkout")
    @PreAuthorize("hasRole('CUSTOMER')")
    @RequiredRoles({"CUSTOMER"})
    @Operation(
            summary = "Checkout from cart items",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Successful Response",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ApiResponseOrderSchema.class)

                            )
                    )
            }
    )
    @ApiUnauthorizedResponse()
    @ApiValidationErrorResponse()
    @ApiNotFoundResponse(description = "Cart item not found", message = "Cart item not found")
    public ResponseEntity<OrderDto> checkout(@CurrentUser User user, @Valid @RequestBody OrderRequestDto request) {
        OrderDto response = orderService.checkout(user, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    @PreAuthorize("hasRole('CUSTOMER')")
    @RequiredRoles({"CUSTOMER"})
    @Operation(
            summary = "Get all user orders",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successful Response",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ApiResponsePageOrderSchema.class)

                            )
                    )
            }
    )
    @ApiUnauthorizedResponse()
    @ApiValidationErrorResponse()
    public ResponseEntity<PageDto<OrderDto>> getAllUserOrders(@CurrentUser User user, Pageable pageable) {
        PageDto<OrderDto> response = orderService.getAllUserOrders(user, pageable);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/{order_id}")
    @PreAuthorize("hasRole('CUSTOMER')")
    @RequiredRoles({"CUSTOMER"})
    @Operation(
            summary = "Get order details by ID",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Successful Response",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ApiResponseOrderSchema.class)
                            )
                    )
            }
    )
    @ApiUnauthorizedResponse()
    @ApiValidationErrorResponse()
    @ApiNotFoundResponse(description = "Order not found", message = "Order not found")
    public ResponseEntity<OrderDto> getOrderById(@PathVariable("order_id") UUID orderId) {
        OrderDto response = orderService.getOrderById(orderId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }


}
