package com.tamamhuda.minimart.application.service.impl;

import com.tamamhuda.minimart.application.dto.PaymentDto;
import com.tamamhuda.minimart.application.dto.xendit.InvoiceRequestDto;
import com.tamamhuda.minimart.application.dto.xendit.ItemsDto;
import com.tamamhuda.minimart.application.mapper.InvoiceItemsMapper;
import com.tamamhuda.minimart.application.mapper.PaymentMapper;
import com.tamamhuda.minimart.application.service.PaymentService;
import com.tamamhuda.minimart.domain.entity.*;
import com.tamamhuda.minimart.domain.enums.OrderStatus;
import com.tamamhuda.minimart.domain.enums.PaymentStatus;
import com.tamamhuda.minimart.domain.repository.PaymentRepository;
import com.xendit.exception.XenditException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final InvoiceItemsMapper invoiceItemsMapper;
    private final OrderServiceImpl orderServiceImpl;

    @Value("${spring.xendit.redirect.success-url}")
    private String successRedirectUrl;

    @Value("${spring.xendit.redirect.failure-url}")
    private String failureRedirectUrl;

    private final PaymentRepository paymentRepository;
    private final PaymentMapper paymentMapper;
    private final InvoiceServiceImpl invoiceService;


    public Payment findById(UUID paymentId) {
        return paymentRepository.findById(paymentId).orElseThrow(
                ()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "Payment not found"));
    }


    private Invoice buildInvoiceRequestAndSend(User user, Order order, Payment payment, List<OrderItem> orderItems) throws XenditException {
        List<ItemsDto> items = invoiceItemsMapper.toDto(orderItems);

        String externalId = "payment-"+payment.getId().toString();

        InvoiceRequestDto request = InvoiceRequestDto.builder()
                .externalId(externalId)
                .amount(order.getTotalPrice())
                .currency("IDR")
                .mobileNumber("+6289501455579")
                .phoneNumber("+6289501455579")
                .invoiceDuration(60 * 60 * 24)
                .items(items)
                .successRedirectUrl(successRedirectUrl)
                .failureRedirectUrl(failureRedirectUrl)
                .build();

      return invoiceService.buildInvoice(user, request);
    };


    private Invoice createInvoiceForPayment(User user, Payment payment, Order order) {
        try {

            List<OrderItem> orderItems = order.getOrderItems();

            Invoice invoice = buildInvoiceRequestAndSend(user, order, payment, orderItems);

            order.attachPayment(payment);

            return invoice;

        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @Override
    @Transactional
    public PaymentDto startPaymentForOrder(User user, UUID orderId) {
        Order order = orderServiceImpl.findById(orderId);

        if (order.getPayment() != null ) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,  "Cannot create payment: order " + orderId + " already has a payment");
        }

        Payment payment = createPayment(order);

        Invoice invoice = createInvoiceForPayment(user, payment, order);

        payment.attachInvoice(invoice);

        Payment updatedPayment = paymentRepository.save(payment);

        return paymentMapper.toDto(updatedPayment);
    }

    private Payment createPayment(Order order) {
        PaymentDto paymentDto = PaymentDto.builder()
                .totalAmount(order.getTotalPrice())
                .status(PaymentStatus.PENDING.name())
                .build();

        Payment payment = paymentMapper.toEntity(paymentDto);

        payment.setOrder(order);

        order.attachPayment(payment);
        order.setStatus(OrderStatus.PENDING_PAYMENT);

        return paymentRepository.save(payment);
    }

    @Override
    @Transactional
    public PaymentDto getPaymentDetails(UUID paymentId, UUID orderId) {
        Order order = orderServiceImpl.findById(orderId);

        Payment payment = findById(paymentId);

        if (!payment.getOrder().equals(order)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Payment with order " + orderId + " not found");
        }

        return paymentMapper.toDto(payment);
    }



}
