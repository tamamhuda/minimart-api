package com.tamamhuda.minimart.application.mapper;

import com.tamamhuda.minimart.application.dto.CartItemRequestDto;
import com.tamamhuda.minimart.domain.entity.CartItem;
import org.mapstruct.*;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface CartItemRequestMapper extends GenericDtoMapper<CartItemRequestDto, CartItem> {

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateFromRequestDto(CartItemRequestDto cartItemRequestDto, @MappingTarget CartItem cartItem);

}
