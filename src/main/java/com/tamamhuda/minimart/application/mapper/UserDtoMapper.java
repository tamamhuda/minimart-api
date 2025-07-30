package com.tamamhuda.minimart.application.mapper;

import com.tamamhuda.minimart.application.dto.UserDto;
import com.tamamhuda.minimart.domain.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserDtoMapper extends GenericDtoMapper<UserDto, User>{
}
