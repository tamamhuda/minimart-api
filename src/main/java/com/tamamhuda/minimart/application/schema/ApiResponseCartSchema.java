package com.tamamhuda.minimart.application.schema;

import com.tamamhuda.minimart.application.dto.CartDto;
import com.tamamhuda.minimart.common.dto.ApiResponseDto;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "ApiResponseOfCartSchema", allOf = {ApiResponseDto.class})
public class ApiResponseCartSchema extends ApiResponseDto<CartDto> {
}
