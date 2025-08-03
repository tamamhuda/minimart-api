package com.tamamhuda.minimart.application.mapper;

import com.tamamhuda.minimart.application.dto.ProductDto;
import com.tamamhuda.minimart.application.dto.ProductRequestDto;
import com.tamamhuda.minimart.domain.entity.Product;
import org.mapstruct.*;

import java.util.List;


@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ProductMapper extends GenericDtoMapper<ProductRequestDto, Product> {

    @Override
    @Mapping(target = "category", ignore = true)
    Product toEntity(ProductRequestDto dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateFromDto(ProductRequestDto productDto, @MappingTarget Product entity);

    ProductDto toResponseDto(Product product);

    List<ProductDto> toResponseDto(List<Product> products);

}
