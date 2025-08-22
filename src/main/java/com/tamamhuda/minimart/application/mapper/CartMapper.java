package com.tamamhuda.minimart.application.mapper;

import com.tamamhuda.minimart.application.dto.CartDto;
import com.tamamhuda.minimart.application.dto.CartItemDto;
import com.tamamhuda.minimart.common.dto.PageDto;
import com.tamamhuda.minimart.domain.entity.Cart;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.ReportingPolicy;
import org.springframework.data.domain.Page;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CartMapper extends GenericDtoMapper<CartDto, Cart> {

    @Override
    @Mappings({
            @Mapping(source = "user.id", target = "userId"),
            @Mapping(target = "cartItems", ignore = true) // dto -> entity
    })
    CartDto toDto(Cart entity);

    @Override
    @Mappings({
            @Mapping(target = "cartItems", ignore = true), // entity -> dto
            @Mapping(target = "user", ignore = true) // if user is set somewhere else
    })
    Cart toEntity(CartDto dto);


    default CartDto toDtoWithPageItems(Cart cart, Page<CartItemDto> page) {
        CartDto dto = toDto(cart);
        PageDto<CartItemDto> pageDto = PageDto.<CartItemDto>builder()
                .content(page.getContent())
                .pageNumber(page.getNumber())
                .pageSize(page.getSize())
                .totalPages(page.getTotalPages())
                .totalElements(page.getTotalElements())
                .last(page.isLast())
                .build();
        dto.setCartItems(pageDto);
        return dto;
    }
}
