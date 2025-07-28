package com.tamamhuda.minimart.common.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class TestDto {

    @NotBlank(message = "Message is required")
    private String message;
}
