package com.tamamhuda.minimart.api.v1.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tamamhuda.minimart.application.service.impl.WebhookServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/webhook")
@RequiredArgsConstructor
public class WebhookController {

    private final ObjectMapper objectMapper;
    private final WebhookServiceImpl webhookServiceImpl;

    @PostMapping("/xendit/payments")
    public ResponseEntity<String> payments(
            @RequestBody Map<String, Object> payload,
            @RequestHeader(value = "X-CALLBACK-TOKEN") String token
    ) {

        return webhookServiceImpl.xenditPayments(payload, token);
    }


}
