package com.tamamhuda.minimart.application.service;

import com.tamamhuda.minimart.application.dto.PaymentDto;
import com.tamamhuda.minimart.domain.entity.Payment;

import java.util.UUID;

public interface PaymentService {

    Payment create(PaymentDto payment);

    Payment getById(UUID paymentId);

    Payment update(PaymentDto payment);

}
