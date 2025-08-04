package com.tamamhuda.minimart.application.mapper;

import com.tamamhuda.minimart.application.dto.RegisterRequestDto;
import com.tamamhuda.minimart.domain.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface UserRequestMapper extends GenericRequestMapper<RegisterRequestDto, User> {
}
