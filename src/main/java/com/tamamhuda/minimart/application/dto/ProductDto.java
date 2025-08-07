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

@AllArgsConstructor
@RequiredArgsConstructor
@Getter
@Setter
@SuperBuilder
@JsonInclude(JsonInclude.Include.ALWAYS)
@JsonPropertyOrder({"id", "name", "description", "price", "stock_quantity", "category", "image_url"})
public class ProductDto extends BaseDto {

    private String name;

    private String description;

    private BigDecimal price;

    @JsonProperty("stock_quantity")
    private Integer stockQuantity;

    private CategoryDto category;

    @JsonProperty("image_url")
    private String imageUrl;

}
