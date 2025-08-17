package com.tamamhuda.minimart.application.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.Instant;

@AllArgsConstructor
@RequiredArgsConstructor
@Getter
@Setter
@SuperBuilder
@JsonInclude(JsonInclude.Include.ALWAYS)
@Schema(name = "PaymentSchema")
@JsonPropertyOrder({"id", "total_amount", "method", "paid_at", "status"})
public class PaymentDto extends BaseDto{

    @JsonProperty("total_amount")
    private BigDecimal totalAmount;

    @JsonProperty("payment_method")
    private String paymentMethod;

    @JsonProperty("paid_at")
    private Instant paidAt;


    private String status;

    private InvoiceDto invoice;

}
