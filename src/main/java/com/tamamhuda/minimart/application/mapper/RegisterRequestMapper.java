package com.tamamhuda.minimart.application.mapper;

import com.tamamhuda.minimart.application.dto.RegisterRequestDto;
import com.tamamhuda.minimart.domain.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RegisterRequestMapper extends GenericDtoMapper<RegisterRequestDto, User> {
}
