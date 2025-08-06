package com.tamamhuda.minimart.application.mapper;

import com.tamamhuda.minimart.application.dto.OrderDto;
import com.tamamhuda.minimart.domain.entity.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface OrderMapper extends GenericDtoMapper<OrderDto, Order> {

    @Override
    @Mappings({
            @Mapping(source = "user.id", target = "userId"),
            @Mapping(source = "orderItems", target = "orderItems"),
            @Mapping(source = "payment.id", target = "paymentId"),
            @Mapping(source = "invoice.id", target = "invoiceId"),

    })
    OrderDto toDto(Order order);
}
