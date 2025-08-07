package com.tamamhuda.minimart.application.service;

import com.tamamhuda.minimart.application.dto.PaymentDto;
import com.tamamhuda.minimart.domain.entity.User;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

public interface PaymentService {

    ResponseEntity<PaymentDto> startPaymentForOrder(User user, UUID orderId);

    ResponseEntity<PaymentDto> getPaymentDetails(UUID paymentId, UUID orderId);
}
