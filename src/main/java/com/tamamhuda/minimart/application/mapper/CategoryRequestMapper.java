package com.tamamhuda.minimart.application.mapper;

import com.tamamhuda.minimart.application.dto.CategoryRequestDto;
import com.tamamhuda.minimart.domain.entity.Category;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface CategoryRequestMapper extends GenericRequestMapper<CategoryRequestDto, Category> {
}
