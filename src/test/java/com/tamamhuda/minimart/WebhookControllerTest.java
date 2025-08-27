package com.tamamhuda.minimart;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tamamhuda.minimart.application.dto.xendit.XenditInvoicePayloadDto;
import com.tamamhuda.minimart.application.service.impl.WebhookServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Map;

import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class WebhookControllerTest {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    @MockitoBean
    private WebhookServiceImpl webhookServiceImpl;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .alwaysDo(print())
                .build();
    }

    @Test
    @DisplayName("POST /webhook/xendit/payments should handle Xendit webhook")
    void testXenditPaymentsWebhook() throws Exception {
        // Prepare payload DTO
        XenditInvoicePayloadDto.PaymentDetailsDto paymentDetails = XenditInvoicePayloadDto.PaymentDetailsDto.builder()
                .source("VA")
                .build();

        XenditInvoicePayloadDto payloadDto = XenditInvoicePayloadDto.builder()
                .paidAt("2025-08-27T10:00:00Z")
                .paidAmount(100)
                .paymentMethod("CREDIT_CARD")
                .paymentChannel("ONLINE")
                .paymentMethodId("PM-123")
                .paymentDetails(paymentDetails)
                .build();

        // Convert to Map for request
        Map<String, Object> payloadMap = objectMapper.convertValue(payloadDto, Map.class);

        String token = "test-xendit-token";

        // Mock service response
        Mockito.when(webhookServiceImpl.xenditPayments(eq(payloadMap), eq(token)))
                .thenReturn(ResponseEntity.ok("OKE"));

        mockMvc.perform(post("/webhook/xendit/payments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(payloadMap))
                        .header("X-CALLBACK-TOKEN", token))
                .andExpect(status().isOk())
                .andExpect(content().string("OKE"));
    }

    @Test
    @DisplayName("POST /webhook/xendit/payments with invalid token should return unauthorized")
    void testXenditPaymentsWebhookInvalidToken() throws Exception {
        XenditInvoicePayloadDto payloadDto = XenditInvoicePayloadDto.builder()
                .paidAt("2025-08-27T10:00:00Z")
                .paidAmount(100)
                .paymentMethod("CREDIT_CARD")
                .paymentChannel("ONLINE")
                .paymentMethodId("PM-123")
                .paymentDetails(XenditInvoicePayloadDto.PaymentDetailsDto.builder()
                        .source("VA")
                        .build())
                .build();

        Map<String, Object> payloadMap = objectMapper.convertValue(payloadDto, Map.class);
        String token = "invalid-token";

        Mockito.when(webhookServiceImpl.xenditPayments(eq(payloadMap), eq(token)))
                .thenReturn(ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Xendit token verification is invalid"));

        mockMvc.perform(post("/webhook/xendit/payments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(payloadMap))
                        .header("X-CALLBACK-TOKEN", token))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Xendit token verification is invalid"));
    }
}