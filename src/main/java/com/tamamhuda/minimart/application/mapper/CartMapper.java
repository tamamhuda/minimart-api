package com.tamamhuda.minimart.application.mapper;

import com.tamamhuda.minimart.application.dto.CartDto;
import com.tamamhuda.minimart.domain.entity.Cart;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface CartMapper extends GenericDtoMapper<CartDto, Cart> {

    @Override
    @Mappings({
                    @Mapping(source = "user.id", target = "userId"),
                    @Mapping(source = "cartItems", target = "cartItems")
    })
    CartDto toDto(Cart entity);
}
