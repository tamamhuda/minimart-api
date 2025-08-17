package com.tamamhuda.minimart.application.schema;

import com.tamamhuda.minimart.application.dto.CartDto;
import com.tamamhuda.minimart.common.dto.ApiResponseDto;
import com.tamamhuda.minimart.common.dto.PageDto;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "ApiResponseOfPageCartSchema", allOf =  {ApiResponsePageCartSchema.class})
public class ApiResponsePageCartSchema extends ApiResponseDto<PageDto<CartDto>> {
}
