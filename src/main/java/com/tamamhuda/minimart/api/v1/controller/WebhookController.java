package com.tamamhuda.minimart.api.v1.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tamamhuda.minimart.application.service.impl.WebhookServiceImpl;
import com.tamamhuda.minimart.common.annotation.ApiUnauthorizedResponse;
import com.tamamhuda.minimart.common.annotation.ApiValidationErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/webhook")
@RequiredArgsConstructor
@Tag(
        name = "Webhook Management",
        description = "Manage all incoming webhook requests"
)
public class WebhookController {

    private final ObjectMapper objectMapper;
    private final WebhookServiceImpl webhookServiceImpl;

    @PostMapping("/xendit/payments")
    @Operation(
            summary = "Handle webhook requests from xendit payment",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successful Response",
                            content = @Content(
                                    mediaType = "plain/text",
                                    examples = @ExampleObject(
                                            value = "OKE"
                                    )

                            )
                    )
            }
    )
    @ApiUnauthorizedResponse(description = "Xendit callback token is invalid", message = "Xendit token verification is invalid")
    @ApiValidationErrorResponse()
    public ResponseEntity<String> payments(
            @RequestBody Map<String, Object> payload,
            @RequestHeader(value = "X-CALLBACK-TOKEN") String token
    ) {

        return webhookServiceImpl.xenditPayments(payload, token);
    }


}
