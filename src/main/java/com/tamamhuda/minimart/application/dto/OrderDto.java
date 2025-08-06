package com.tamamhuda.minimart.application.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@RequiredArgsConstructor
@Getter
@Setter
@SuperBuilder
@JsonInclude(JsonInclude.Include.ALWAYS)
@JsonPropertyOrder({"id", "user_id", "order_items", "payment_id", "invoice_id", "total_price", "status", })
public class OrderDto extends BaseDto {

    @JsonProperty("user_id")
    private UUID userId;

    @JsonProperty("order_items")
    private List<OrderItemDto> orderItems;

    private String status;

    @JsonProperty("total_price")
    private BigDecimal totalPrice;

    @JsonProperty("payment_id")
    private UUID paymentId;

    @JsonProperty("invoice_id")
    private UUID invoiceId;

}
