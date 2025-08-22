package com.tamamhuda.minimart.application.mapper;

import com.tamamhuda.minimart.application.dto.CartItemRequestDto;
import com.tamamhuda.minimart.domain.entity.CartItem;
import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE,  nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface CartItemRequestMapper extends GenericRequestMapper<CartItemRequestDto, CartItem> {
}
