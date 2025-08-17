package com.tamamhuda.minimart.application.schema;

import com.tamamhuda.minimart.application.dto.CategoryDto;
import com.tamamhuda.minimart.common.dto.ApiResponseDto;
import com.tamamhuda.minimart.common.dto.PageDto;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "ApiResponseOfPageCategorySchema", allOf =  {ApiResponseDto.class})
public class ApiResponsePageCategorySchema extends ApiResponseDto<PageDto<CategoryDto>> {
}
