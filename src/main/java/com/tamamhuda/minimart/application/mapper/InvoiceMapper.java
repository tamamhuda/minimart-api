package com.tamamhuda.minimart.application.mapper;

import com.tamamhuda.minimart.application.dto.InvoiceDto;
import com.tamamhuda.minimart.domain.entity.Invoice;
import org.mapstruct.*;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy =  NullValuePropertyMappingStrategy.IGNORE, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface InvoiceMapper extends GenericDtoMapper<InvoiceDto, Invoice> {

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateFromDto(InvoiceDto invoiceDto, @MappingTarget Invoice invoice);
}
