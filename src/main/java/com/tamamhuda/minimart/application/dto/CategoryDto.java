package com.tamamhuda.minimart.application.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@JsonInclude(JsonInclude.Include.ALWAYS)
@Schema(name = "CategorySchema")
@JsonPropertyOrder({"id", "name"})
public class CategoryDto {

    private String id;

    @NotBlank(message = "name is required")
    private String name;

    private String description;

}
