package com.tamamhuda.minimart.application.service.impl;

import com.tamamhuda.minimart.application.dto.PaymentDto;
import com.tamamhuda.minimart.application.mapper.PaymentMapper;
import com.tamamhuda.minimart.application.service.PaymentService;
import com.tamamhuda.minimart.domain.entity.Payment;
import com.tamamhuda.minimart.domain.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final PaymentMapper paymentMapper;

    @Override
    public Payment create(PaymentDto paymentDto) {
        Payment payment = paymentMapper.toEntity(paymentDto);

        return paymentRepository.save(payment);
    }

    @Override
    public Payment getById(UUID paymentId) {
        return paymentRepository.findById(paymentId).orElseThrow(
                ()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "Payment not found"));
    }

    @Override
    public Payment update(PaymentDto paymentDto) {
        UUID paymentId = UUID.fromString(paymentDto.getId());
        Payment existingPayment = getById(paymentId);
        paymentMapper.updateFromDto(paymentDto, existingPayment);

        return paymentRepository.save(existingPayment);
    }
}
