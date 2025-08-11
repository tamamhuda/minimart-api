package com.tamamhuda.minimart.application.dto;

import com.tamamhuda.minimart.domain.enums.OtpStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@AllArgsConstructor
@RequiredArgsConstructor
@Getter
@Setter
@SuperBuilder
public class VerifyDto {
    private OtpStatus status;
    private String message;
}
