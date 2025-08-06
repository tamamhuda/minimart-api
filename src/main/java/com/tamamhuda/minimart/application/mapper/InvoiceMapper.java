package com.tamamhuda.minimart.application.mapper;

import com.tamamhuda.minimart.application.dto.InvoiceDto;
import com.tamamhuda.minimart.domain.entity.Invoice;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy =  NullValuePropertyMappingStrategy.IGNORE)
public interface InvoiceMapper extends GenericDtoMapper<InvoiceDto, Invoice> {

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateFromDto(InvoiceDto invoiceDto, @MappingTarget Invoice invoice);
}
