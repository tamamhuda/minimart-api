package com.tamamhuda.minimart.application.schema;

import com.tamamhuda.minimart.application.dto.UserDto;
import com.tamamhuda.minimart.common.dto.ApiResponseDto;
import com.tamamhuda.minimart.common.dto.PageDto;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "ApiResponseOfPageUserSchema", allOf = { ApiResponseDto.class })
public class ApiResponsePageUserSchema extends ApiResponseDto<PageDto<UserDto>> {
}
