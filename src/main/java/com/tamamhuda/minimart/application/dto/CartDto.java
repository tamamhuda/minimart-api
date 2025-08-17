package com.tamamhuda.minimart.application.dto;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.tamamhuda.minimart.common.dto.PageDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@AllArgsConstructor
@RequiredArgsConstructor
@Getter
@Setter
@SuperBuilder
@JsonInclude(JsonInclude.Include.ALWAYS)
@Schema(name = "CartSchema")
@JsonPropertyOrder({"id", "user_id", "cart_items"})
public class CartDto extends BaseDto {

    @JsonProperty("user_id")
    private UUID userId;

    @JsonProperty("cart_items")
    private PageDto<CartItemDto> cartItems;
}
