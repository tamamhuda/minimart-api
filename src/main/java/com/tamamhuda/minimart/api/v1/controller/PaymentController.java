package com.tamamhuda.minimart.api.v1.controller;

import com.tamamhuda.minimart.application.dto.PaymentDto;
import com.tamamhuda.minimart.application.schema.ApiResponsePaymentSchema;
import com.tamamhuda.minimart.application.service.impl.PaymentServiceImpl;
import com.tamamhuda.minimart.common.annotation.*;
import com.tamamhuda.minimart.domain.entity.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("orders/{order_id}/payments")
@RequiredArgsConstructor
@Validated
@Tag(
        name = "Order Management",
        description = "manage all user orders"
)
@SecurityRequirement(name = "AuthorizationHeader")
public class PaymentController {
    private final PaymentServiceImpl paymentService;

    @PostMapping()
    @PreAuthorize("hasRole('CUSTOMER')")
    @RequiredRoles({"CUSTOMER"})
    @Operation(
            summary = "Start or create payment for an order",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Successful Response",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ApiResponsePaymentSchema.class)
                            )
                    )
            }
    )
    @ApiUnauthorizedResponse()
    @ApiValidationErrorResponse()
    @ApiNotFoundResponse(description = "Order not found", message = "Order not found")
    public ResponseEntity<PaymentDto> startPaymentForOrder(@CurrentUser User user, @PathVariable("order_id") UUID orderId) {
        PaymentDto response = paymentService.startPaymentForOrder(user, orderId);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{payment_id}")
    @PreAuthorize("hasRole('CUSTOMER')")
    @RequiredRoles({"CUSTOMER"})
    @Operation(
            summary = "Get payment details of an order",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Successful Response",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ApiResponsePaymentSchema.class)
                            )
                    )
            }
    )
    @ApiUnauthorizedResponse()
    @ApiValidationErrorResponse()
    @ApiNotFoundResponse(description = "Payment or Order not found", message = "Payment not found")
    public ResponseEntity<PaymentDto> getPaymentDetails(
            @PathVariable("payment_id") UUID paymentId,
            @PathVariable("order_id") UUID orderId
            ) {
        PaymentDto response = paymentService.getPaymentDetails(paymentId, orderId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}
