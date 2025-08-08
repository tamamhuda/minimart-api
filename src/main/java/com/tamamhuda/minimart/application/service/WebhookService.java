package com.tamamhuda.minimart.application.service;

import org.springframework.http.ResponseEntity;

import java.util.Map;

public interface WebhookService {
    ResponseEntity<String> xenditPayments(Map<String, Object> payload, String token);
}
