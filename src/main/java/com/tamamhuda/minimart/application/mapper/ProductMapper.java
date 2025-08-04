package com.tamamhuda.minimart.application.mapper;

import com.tamamhuda.minimart.application.dto.ProductDto;
import com.tamamhuda.minimart.domain.entity.Product;
import org.mapstruct.*;


@Mapper(componentModel = "spring")
public interface ProductMapper extends GenericDtoMapper<ProductDto, Product> {

}
