package com.tamamhuda.minimart.application.mapper;

import com.tamamhuda.minimart.application.dto.CategoryDto;
import com.tamamhuda.minimart.domain.entity.Category;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CategoryMapper extends GenericDtoMapper<CategoryDto, Category> {

}
