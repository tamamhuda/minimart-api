package com.tamamhuda.minimart.application.mapper;

import com.tamamhuda.minimart.application.dto.ProductRequestDto;
import com.tamamhuda.minimart.domain.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ProductRequestMapper extends GenericRequestMapper<ProductRequestDto, Product> {
}
