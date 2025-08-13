package com.tamamhuda.minimart.application.mapper;

import com.tamamhuda.minimart.application.dto.UserDto;
import com.tamamhuda.minimart.domain.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper extends GenericDtoMapper<UserDto, User>{

    @Override
    @Mapping(source = "verified", target = "isVerified") // entity → DTO
    UserDto toDto(User entity);
}
