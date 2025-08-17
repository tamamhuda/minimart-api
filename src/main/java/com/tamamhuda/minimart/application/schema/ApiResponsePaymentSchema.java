package com.tamamhuda.minimart.application.schema;

import com.tamamhuda.minimart.application.dto.PaymentDto;
import com.tamamhuda.minimart.common.dto.ApiResponseDto;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "ApiResponseOfPaymentSchema", allOf = {ApiResponseDto.class})
public class ApiResponsePaymentSchema extends ApiResponseDto<PaymentDto> {
}
