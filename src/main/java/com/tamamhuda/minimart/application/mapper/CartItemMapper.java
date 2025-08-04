package com.tamamhuda.minimart.application.mapper;

import com.tamamhuda.minimart.application.dto.CartItemDto;
import com.tamamhuda.minimart.domain.entity.CartItem;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CartItemMapper extends GenericDtoMapper<CartItemDto, CartItem> {
}
