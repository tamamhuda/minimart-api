package com.tamamhuda.minimart.application.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tamamhuda.minimart.application.dto.xendit.XenditInvoicePayloadDto;
import com.tamamhuda.minimart.application.service.WebhookService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class WebhookServiceImpl implements WebhookService {

    private final ObjectMapper objectMapper;
    private final InvoiceServiceImpl invoiceService;

    @Value("${spring.xendit.webhook-verification-token}")
    private String xenditVerificationToken;

    @Override
    @Transactional
    public ResponseEntity<String> xenditPayments(Map<String, Object> payload, String token) {

        if (!token.equals(xenditVerificationToken)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid Token");
        }

        XenditInvoicePayloadDto paymentPayload = objectMapper.convertValue(payload, XenditInvoicePayloadDto.class);

        return invoiceService.webhookInvoiceHandler(paymentPayload);
    }
}
