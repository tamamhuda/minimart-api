package com.tamamhuda.minimart.application.schema;

import com.tamamhuda.minimart.application.dto.RefreshTokenDto;
import com.tamamhuda.minimart.common.dto.ApiResponseDto;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "ApiResponseOfRefreshTokenSchema", allOf = {ApiResponseDto.class})
public class ApiResponseRefreshTokenSchema extends ApiResponseDto<RefreshTokenDto> {
}
