package com.tamamhuda.minimart.application.mapper;

import com.tamamhuda.minimart.application.dto.SessionDto;
import com.tamamhuda.minimart.domain.entity.Session;
import org.mapstruct.*;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SessionMapper extends GenericDtoMapper<SessionDto, Session> {

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateSessionFromDto(SessionDto sessionDto, @MappingTarget Session entity);
}
