package com.tamamhuda.minimart.application.mapper;

import com.tamamhuda.minimart.application.dto.OrderDto;
import com.tamamhuda.minimart.domain.entity.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface OrderMapper extends GenericDtoMapper<OrderDto, Order> {

    @Override
    @Mappings({
            @Mapping(source = "orderItems", target = "orderItems"),
            @Mapping(source = "payment", target = "payment"),
            @Mapping(source = "user", target = "user")
    })
    OrderDto toDto(Order order);
}
