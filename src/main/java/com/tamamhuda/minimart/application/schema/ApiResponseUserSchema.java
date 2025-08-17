package com.tamamhuda.minimart.application.schema;

import com.tamamhuda.minimart.application.dto.UserDto;
import com.tamamhuda.minimart.common.dto.ApiResponseDto;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "ApiResponseOfUserSchema", allOf = { ApiResponseDto.class })
public class ApiResponseUserSchema extends ApiResponseDto<UserDto> {
}
