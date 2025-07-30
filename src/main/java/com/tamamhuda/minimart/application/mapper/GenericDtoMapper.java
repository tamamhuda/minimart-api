package com.tamamhuda.minimart.application.mapper;

public interface GenericDtoMapper<D, E>{
    E toEntity(D dto);
    D toDto(E entity);
}
