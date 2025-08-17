package com.tamamhuda.minimart.application.schema;

import com.tamamhuda.minimart.application.dto.ProductDto;
import com.tamamhuda.minimart.common.dto.ApiResponseDto;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "ApiResponseOfProductSchema", allOf = {ApiResponseDto.class})
public class ApiResponseProductSchema extends ApiResponseDto<ProductDto> {
}
