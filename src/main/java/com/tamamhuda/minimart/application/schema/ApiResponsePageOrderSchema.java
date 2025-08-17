package com.tamamhuda.minimart.application.schema;

import com.tamamhuda.minimart.application.dto.OrderDto;
import com.tamamhuda.minimart.common.dto.ApiResponseDto;
import com.tamamhuda.minimart.common.dto.PageDto;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "ApiResponseOfPageOrderSchema", allOf = {ApiResponseDto.class})
public class ApiResponsePageOrderSchema extends ApiResponseDto<PageDto<OrderDto>> {
}
