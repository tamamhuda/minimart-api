package com.tamamhuda.minimart.application.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

public interface GenericRequestMapper<D, E> extends GenericDtoMapper<D, E> {

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateFromRequestDto(D dto, @MappingTarget E entity);
}
