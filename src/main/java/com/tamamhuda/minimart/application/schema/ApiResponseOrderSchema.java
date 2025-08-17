package com.tamamhuda.minimart.application.schema;

import com.tamamhuda.minimart.application.dto.OrderDto;
import com.tamamhuda.minimart.common.dto.ApiResponseDto;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "ApiResponseOfOrderSchema", allOf = {ApiResponseDto.class})
public class ApiResponseOrderSchema extends ApiResponseDto<OrderDto> {
}
