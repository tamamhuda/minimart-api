package com.tamamhuda.minimart.application.mapper;

import com.tamamhuda.minimart.application.dto.xendit.ItemsDto;
import com.tamamhuda.minimart.domain.entity.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.ReportingPolicy;


@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface InvoiceItemsMapper extends GenericDtoMapper<ItemsDto, OrderItem>{

    default Integer map(Number value) {
        return value != null ? value.intValue() : null;
    }

    @Override
    @Mappings({
            @Mapping(source = "product.name", target = "name"),
            @Mapping(source = "product.category.name", target = "category"),
            @Mapping(source = "quantity", target = "quantity"),
            @Mapping(source = "product.price", target = "price")
    })
    ItemsDto toDto(OrderItem entity);

}
