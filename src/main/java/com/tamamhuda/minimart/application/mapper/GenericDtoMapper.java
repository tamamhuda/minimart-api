package com.tamamhuda.minimart.application.mapper;

import java.util.List;

public interface GenericDtoMapper<D, E>{
    E toEntity(D dto);
    D toDto(E entity);
    List<D> toDto(List<E> entities);
}
