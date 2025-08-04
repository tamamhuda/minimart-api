package com.tamamhuda.minimart.application.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.tamamhuda.minimart.common.validation.group.Create;
import com.tamamhuda.minimart.common.validation.group.Update;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.validator.constraints.UUID;

@AllArgsConstructor
@RequiredArgsConstructor
@Getter
@Setter
@SuperBuilder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CartItemRequestDto {

    @JsonProperty("product_id")
    @UUID(message = "product_id must be valid uuid",  groups = {Create.class})
    @NotBlank(message = "product_id is required", groups = {Create.class})
    private String productId;

    @Positive(message = "quantity must be a positive integer", groups = {Create.class, Update.class})
    @NotNull(message = "quantity is required", groups = {Create.class, Update.class})
    private Integer quantity;

}
