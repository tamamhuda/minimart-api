package com.tamamhuda.minimart.application.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.tamamhuda.minimart.common.validation.group.Create;
import com.tamamhuda.minimart.common.validation.group.Update;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@RequiredArgsConstructor
@Getter
@Setter
@SuperBuilder
@Schema(name = "ProductRequestSchema")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductRequestDto extends BaseDto {

    @NotBlank(message = "name is required", groups = {Create.class})
    private String name;

    @NotBlank(message = "description is required", groups = {Create.class})
    private String description;

    @NotNull(message = "price is required", groups = {Create.class})
    @DecimalMin(value = "0.0", inclusive = false, message = "price must be greater than 0", groups = {Create.class})
    private Float price;

    @NotNull(message = "stock_quantity is required", groups = {Create.class})
    @Positive(message = "stock_quantity must be positive integer", groups = {Create.class, Update.class})
    @JsonProperty("stock_quantity")
    private Integer stockQuantity;

    @NotBlank(message = "category is required", groups = {Create.class})
    @JsonProperty("category")
    private String categoryIdOrName;

    @JsonProperty("image_url")
    private String imageUrl;

}
