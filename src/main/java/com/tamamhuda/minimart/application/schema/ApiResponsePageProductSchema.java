package com.tamamhuda.minimart.application.schema;

import com.tamamhuda.minimart.application.dto.ProductDto;
import com.tamamhuda.minimart.common.dto.ApiResponseDto;
import com.tamamhuda.minimart.common.dto.PageDto;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "ApiResponseOfPageProductSchema", allOf = { ApiResponseDto.class })
public class ApiResponsePageProductSchema extends ApiResponseDto<PageDto<ProductDto>> {
}
