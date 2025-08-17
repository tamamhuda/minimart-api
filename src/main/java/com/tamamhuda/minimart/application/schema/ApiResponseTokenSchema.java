package com.tamamhuda.minimart.application.schema;

import com.tamamhuda.minimart.application.dto.TokenDto;
import com.tamamhuda.minimart.common.dto.ApiResponseDto;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "ApiResponseOfTokenSchema", allOf = { ApiResponseDto.class })
public class ApiResponseTokenSchema extends ApiResponseDto<TokenDto> {
}
