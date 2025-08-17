package com.tamamhuda.minimart.application.schema;

import com.tamamhuda.minimart.application.dto.CartItemDto;
import com.tamamhuda.minimart.common.dto.ApiResponseDto;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "ApiResponseOfCartItemSchema", allOf = {ApiResponseDto.class})
public class ApiResponseCartItemSchema extends ApiResponseDto<CartItemDto> {
}
