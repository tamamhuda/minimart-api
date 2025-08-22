package com.tamamhuda.minimart.application.mapper;

import com.tamamhuda.minimart.application.dto.UserRequestDto;
import com.tamamhuda.minimart.domain.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface UserRequestMapper extends GenericRequestMapper<UserRequestDto, User> {

}
