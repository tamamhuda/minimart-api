package com.tamamhuda.minimart.application.schema;

import com.tamamhuda.minimart.application.dto.CategoryDto;
import com.tamamhuda.minimart.common.dto.ApiResponseDto;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "ApiResponseOfPageCategorySchema", allOf = { ApiResponseDto.class })
public class ApiResponseCategorySchema extends ApiResponseDto<CategoryDto> {
}
