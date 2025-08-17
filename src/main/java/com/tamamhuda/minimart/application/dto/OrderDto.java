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
import java.util.List;

@AllArgsConstructor
@RequiredArgsConstructor
@Getter
@Setter
@SuperBuilder
@JsonInclude(JsonInclude.Include.ALWAYS)
@Schema(name = "OrderSchema")
@JsonPropertyOrder({"id", "user", "items", "payment", "total_price", "status", })
public class OrderDto extends BaseDto {

    private UserSummaryDto user;

    @JsonProperty("items")
    private List<OrderItemDto> orderItems;

    private String status;

    @JsonProperty("total_price")
    private BigDecimal totalPrice;

    private PaymentSummaryDto payment;

}
