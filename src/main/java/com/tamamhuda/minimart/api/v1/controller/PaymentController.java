package com.tamamhuda.minimart.api.v1.controller;

import com.tamamhuda.minimart.application.dto.PaymentDto;
import com.tamamhuda.minimart.application.service.impl.PaymentServiceImpl;
import com.tamamhuda.minimart.common.annotation.CurrentUser;
import com.tamamhuda.minimart.common.annotation.RequiredRoles;
import com.tamamhuda.minimart.domain.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("orders/{order_id}/payments")
@RequiredArgsConstructor
@Validated
public class PaymentController {
    private final PaymentServiceImpl paymentService;

    @PostMapping()
    @PreAuthorize("hasRole('CUSTOMER')")
    @RequiredRoles({"CUSTOMER"})
    public ResponseEntity<PaymentDto> startPaymentForOrder(@CurrentUser User user, @PathVariable("order_id") UUID orderId) {
        return paymentService.startPaymentForOrder(user, orderId);
    }

    @GetMapping("/{payment_id}")
    @PreAuthorize("hasRole('CUSTOMER')")
    @RequiredRoles({"CUSTOMER"})
    public ResponseEntity<PaymentDto> getPaymentDetails(
            @PathVariable("payment_id") UUID paymentId,
            @PathVariable("order_id") UUID orderId
            ) {
        return paymentService.getPaymentDetails(paymentId, orderId);
    }

}
