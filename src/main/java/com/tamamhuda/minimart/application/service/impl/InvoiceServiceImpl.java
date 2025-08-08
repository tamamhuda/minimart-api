package com.tamamhuda.minimart.application.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tamamhuda.minimart.application.dto.InvoiceDto;
import com.tamamhuda.minimart.application.dto.xendit.CustomerDto;
import com.tamamhuda.minimart.application.dto.xendit.InvoiceRequestDto;
import com.tamamhuda.minimart.application.dto.xendit.XenditInvoiceDto;
import com.tamamhuda.minimart.application.dto.xendit.XenditInvoicePayloadDto;
import com.tamamhuda.minimart.application.mapper.InvoiceCustomerMapper;
import com.tamamhuda.minimart.application.mapper.InvoiceMapper;
import com.tamamhuda.minimart.application.service.InvoiceService;
import com.tamamhuda.minimart.domain.entity.Invoice;
import com.tamamhuda.minimart.domain.entity.Order;
import com.tamamhuda.minimart.domain.entity.Payment;
import com.tamamhuda.minimart.domain.entity.User;
import com.tamamhuda.minimart.domain.enums.InvoiceStatus;
import com.tamamhuda.minimart.domain.enums.OrderStatus;
import com.tamamhuda.minimart.domain.enums.PaymentMethod;
import com.tamamhuda.minimart.domain.enums.PaymentStatus;
import com.tamamhuda.minimart.domain.repository.InvoiceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class InvoiceServiceImpl implements InvoiceService {
    private final InvoiceRepository invoiceRepository;
    private final InvoiceMapper invoiceMapper;
    private final XenditServiceImpl xenditService;
    private final InvoiceCustomerMapper invoiceCustomerMapper;
    private final ObjectMapper objectMapper;


    @Override
    public Invoice buildInvoice(User user, InvoiceRequestDto request) {
        CustomerDto customer = invoiceCustomerMapper.toDto(xenditService.createCustomer(user));

        XenditInvoiceDto xenditInvoice = xenditService.createXenditInvoice(customer, request);

        InvoiceDto invoiceDto = InvoiceDto.builder()
                .customerName(customer.getGivenNames())
                .customerEmail(customer.getEmail())
                .expiryDate(xenditInvoice.getExpiryDate())
                .externalId(xenditInvoice.getExternalId())
                .invoiceUrl(xenditInvoice.getInvoiceUrl())
                .totalAmount(new BigDecimal(xenditInvoice.getAmount().toString()))
                .status(InvoiceStatus.PENDING.name())
                .issuedDate(Instant.now())
                .build();

        return invoiceMapper.toEntity(invoiceDto);
    }

    @Override
    public Invoice getById(UUID invoiceId) {
        return invoiceRepository.findById(invoiceId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Invoice not found")
        );
    }

    @Override
    public Invoice updateInvoice(InvoiceDto invoiceDto, UUID invoiceId) {
        Invoice existingInvoice = getById(invoiceId);
        invoiceMapper.updateFromDto(invoiceDto, existingInvoice);

        return invoiceRepository.save(existingInvoice);
    }


    private Invoice findInvoiceByExternalId(String externalId) {
        return invoiceRepository.findByExternalId(externalId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Invoice not found")
        );
    }

    private String payloadStringify(XenditInvoicePayloadDto payload) {
        try {
            return objectMapper.writeValueAsString(payload);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    private ResponseEntity<String> invoicePaidHandler(XenditInvoicePayloadDto payload, Invoice invoice) {
        Instant now = Instant.now();
        Payment payment = invoice.getPayment();
        Instant paidAt = Instant.parse(payload.getPaidAt());
        Order order = payment.getOrder();

        invoice.setStatus(InvoiceStatus.PAID);
        invoice.setXenditInvoicePayload(payloadStringify(payload));
        invoice.setUpdatedAt(now);

        payment.setPaidAt(paidAt);
        payment.setPaymentMethod(PaymentMethod.valueOf(payload.getPaymentMethod()));
        payment.setStatus(PaymentStatus.SUCCEEDED);
        payment.setUpdatedAt(now);

        order.setStatus(OrderStatus.PAID);
        order.setUpdatedAt(now);

        invoiceRepository.save(invoice);

        return ResponseEntity.ok("OK");
    }



    private ResponseEntity<String> invoiceExpiredHandler(XenditInvoicePayloadDto payload, Invoice invoice) {
        Instant now = Instant.now();
        Payment payment = invoice.getPayment();
        Order order = payment.getOrder();


        invoice.setStatus(InvoiceStatus.EXPIRED);
        invoice.setXenditInvoicePayload(payloadStringify(payload));
        invoice.setUpdatedAt(now);

        payment.setStatus(PaymentStatus.FAILED);
        payment.setUpdatedAt(now);

        order.setStatus(OrderStatus.EXPIRED);
        order.setUpdatedAt(now);

        invoiceRepository.save(invoice);

        return ResponseEntity.ok("OK");

    }

    private ResponseEntity<String> invoiceFailedHandler(Invoice invoice) {
        Payment payment = invoice.getPayment();

        payment.setStatus(PaymentStatus.FAILED);
        payment.setUpdatedAt(Instant.now());

        invoiceRepository.save(invoice);

        return ResponseEntity.ok("OK");
    }

    @Override
    public ResponseEntity<String> webhookInvoiceHandler(XenditInvoicePayloadDto payload)  {

            String status = payload.getStatus();
            String externalId = payload.getExternalId();

            Invoice invoice = findInvoiceByExternalId(externalId);

            return switch (status) {
                case "PAID" -> invoicePaidHandler(payload, invoice);
                case "EXPIRED" -> invoiceExpiredHandler(payload, invoice);
                default -> invoiceFailedHandler(invoice);
            };

    }
}
