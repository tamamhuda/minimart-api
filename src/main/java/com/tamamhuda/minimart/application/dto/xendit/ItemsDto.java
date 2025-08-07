package com.tamamhuda.minimart.application.dto.xendit;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@AllArgsConstructor
@RequiredArgsConstructor
@Setter
@Getter
@SuperBuilder
public class ItemsDto {

    private String name;

    private Number price;

    private String category;

    @JsonProperty("quantity")
    private Number quantity;

    private String url;
}
