package com.tamamhuda.minimart.application.service;

import com.tamamhuda.minimart.application.dto.PaymentDto;
import com.tamamhuda.minimart.domain.entity.Payment;
import com.tamamhuda.minimart.domain.entity.User;

import java.util.UUID;

public interface PaymentService {

    Payment findById(UUID id);

    PaymentDto startPaymentForOrder(User user, UUID orderId);

    PaymentDto getPaymentDetails(UUID paymentId, UUID orderId);

}
